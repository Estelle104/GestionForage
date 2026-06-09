#!/usr/bin/env bash
# set -euo pipefail

# deploy_and_run.sh - compile, deploy WAR to local Tomcat and start it
# Usage: ./deploy_and_run.sh [TOMCAT_HOME]
# If TOMCAT_HOME not provided, the script uses $TOMCAT_HOME env or common locations.

PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$PROJECT_DIR"

usage(){
  echo "Usage: $0 [TOMCAT_HOME]"
  echo "Example: TOMCAT_HOME=/opt/tomcat ./deploy_and_run.sh"
  exit 1
}

if [ "$#" -gt 1 ]; then
  usage
fi

if [ $# -eq 1 ]; then
  TOMCAT_HOME="$1"
elif [ -n "${TOMCAT_HOME:-}" ]; then
  TOMCAT_HOME="$TOMCAT_HOME"
else
  CATALINA="$(command -v catalina.sh || true)"
  if [ -n "$CATALINA" ]; then
    TOMCAT_HOME="$(dirname "$(dirname "$CATALINA")")"
  else
    TOMCAT_HOME="$(ls -d /opt/*tomcat* /usr/local/*tomcat* /usr/share/*tomcat* /home/$(whoami)/*tomcat* 2>/dev/null | head -n1 || true)"
  fi
fi

if [ -z "${TOMCAT_HOME:-}" ] || [ ! -d "$TOMCAT_HOME" ]; then
  echo "Tomcat introuvable. Définissez TOMCAT_HOME ou installez Tomcat 10." >&2
  usage
fi

echo "Tomcat détecté: $TOMCAT_HOME"
if [ -x "$TOMCAT_HOME/bin/version.sh" ]; then
  echo "--- Tomcat version ---"
  "$TOMCAT_HOME/bin/version.sh" || true
fi

echo "--- Maven: build package ---"
MVN="mvn"
if ! command -v mvn >/dev/null 2>&1 && [ -x "/opt/maven/bin/mvn" ]; then
  MVN="/opt/maven/bin/mvn"
fi
$MVN -DskipTests clean package

WAR="$PROJECT_DIR/target/forage.war"
if [ ! -f "$WAR" ]; then
  echo "Fichier WAR introuvable: $WAR" >&2
  exit 2
fi

echo "--- Déployer $WAR → $TOMCAT_HOME/webapps/ ---"
if [ -x "$TOMCAT_HOME/bin/shutdown.sh" ]; then
  echo "Arrêt de Tomcat..."
  "$TOMCAT_HOME/bin/shutdown.sh" || true
  sleep 2
fi

cp -f "$WAR" "$TOMCAT_HOME/webapps/"
chmod 644 "$TOMCAT_HOME/webapps/$(basename "$WAR")" || true

echo "Démarrage de Tomcat..."
if [ -x "$TOMCAT_HOME/bin/startup.sh" ]; then
  setsid "$TOMCAT_HOME/bin/startup.sh" || true
fi

# Attendre que Tomcat démarre
echo "Attente du démarrage de Tomcat (10 secondes)..."
sleep 10

URL="http://localhost:8080/forage"
echo "Application déployée — essayez: $URL"

# Ouvrir le navigateur
echo "Ouverture du navigateur..."
if command -v xdg-open >/dev/null 2>&1; then
  xdg-open "$URL" >/dev/null 2>&1 &
elif command -v firefox >/dev/null 2>&1; then
  firefox "$URL" >/dev/null 2>&1 &
elif command -v google-chrome >/dev/null 2>&1; then
  google-chrome "$URL" >/dev/null 2>&1 &
elif command -v chromium >/dev/null 2>&1; then
  chromium "$URL" >/dev/null 2>&1 &
else
  echo "Aucun navigateur trouvé - veuillez ouvrir manuellement: $URL"
fi

echo ""
echo "✓ Application lancée avec succès!"
echo "✓ Navigateur ouvert: $URL"
echo ""
echo "Suivi des logs (ctrl+C pour quitter) : $TOMCAT_HOME/logs/catalina.out"
exec tail -n 200 -f "$TOMCAT_HOME/logs/catalina.out"
