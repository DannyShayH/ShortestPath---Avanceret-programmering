#!/bin/zsh

set -u

project_dir="${0:A:h}"
cd "$project_dir" || {
  echo "Kunne ikke åbne projektmappen: $project_dir"
  read -r "?Tryk Enter for at lukke..."
  exit 1
}

if ! command -v python3 >/dev/null 2>&1; then
  echo "Python 3 blev ikke fundet. Installer Python 3 og prøv igen."
  read -r "?Tryk Enter for at lukke..."
  exit 1
fi

if [[ ! -f "astar.html" ]]; then
  echo "astar.html blev ikke fundet i: $project_dir"
  read -r "?Tryk Enter for at lukke..."
  exit 1
fi

if [[ ! -d "/Applications/DuckDuckGo.app" ]]; then
  echo "DuckDuckGo blev ikke fundet i mappen Programmer (/Applications)."
  echo "Installer DuckDuckGo, og prøv derefter igen."
  read -r "?Tryk Enter for at lukke..."
  exit 1
fi

port=$(python3 -c 'import socket; sock = socket.socket(); sock.bind(("127.0.0.1", 0)); print(sock.getsockname()[1]); sock.close()')
if [[ -z "$port" ]]; then
  echo "Kunne ikke finde en ledig lokal port."
  read -r "?Tryk Enter for at lukke..."
  exit 1
fi

server_pid=""
stop_server() {
  if [[ -n "$server_pid" ]] && kill -0 "$server_pid" >/dev/null 2>&1; then
    kill "$server_pid" >/dev/null 2>&1
    wait "$server_pid" >/dev/null 2>&1
  fi
}
trap stop_server EXIT INT TERM HUP

python3 -m http.server "$port" --bind 127.0.0.1 >/dev/null 2>&1 &
server_pid=$!
url="http://127.0.0.1:${port}/astar.html"
server_ready=false

for attempt in {1..50}; do
  if ! kill -0 "$server_pid" >/dev/null 2>&1; then
    echo "Den lokale webserver kunne ikke starte."
    read -r "?Tryk Enter for at lukke..."
    exit 1
  fi
  if curl --silent --fail --output /dev/null "$url"; then
    server_ready=true
    break
  fi
  sleep 0.1
done

if [[ "$server_ready" != true ]]; then
  echo "Den lokale webserver svarede ikke."
  read -r "?Tryk Enter for at lukke..."
  exit 1
fi

if ! open -a "/Applications/DuckDuckGo.app" "$url"; then
  echo "DuckDuckGo kunne ikke åbnes."
  read -r "?Tryk Enter for at lukke..."
  exit 1
fi

echo ""
echo "A*-visualiseringen kører nu i DuckDuckGo:"
echo "$url"
echo ""
echo "Lad dette vindue være åbent, mens du bruger visualiseringen."
echo "Tryk Ctrl+C eller luk vinduet for at stoppe serveren."

wait "$server_pid"
