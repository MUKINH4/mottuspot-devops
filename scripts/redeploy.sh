#!/bin/bash

# Script para rebuild e redeploy rápido da aplicação

RESOURCE_GROUP=rg-mottu-spot
ACR_NAME=mottuspot
APP_IMAGE_NAME=mottu-spot
APP_TAG=latest

echo "🔄 Fazendo rebuild e redeploy da aplicação..."

# Fazer login no ACR
echo "🔑 Fazendo login no ACR..."
az acr login --name $ACR_NAME

# Build da imagem localmente
echo "🏗️ Construindo nova imagem..."
docker build -t $ACR_NAME.azurecr.io/$APP_IMAGE_NAME:$APP_TAG .

# Push da nova imagem
echo "📤 Enviando nova imagem para o ACR..."
docker push $ACR_NAME.azurecr.io/$APP_IMAGE_NAME:$APP_TAG

# Parar o container atual
echo "🛑 Parando container atual..."
az container stop --resource-group $RESOURCE_GROUP --name aci-app-mottu-spot

# Deletar o container atual
echo "🗑️ Deletando container atual..."
az container delete --resource-group $RESOURCE_GROUP --name aci-app-mottu-spot --yes

# Aguardar um pouco para garantir que foi deletado
sleep 10

# Obter FQDN do banco
DB_FQDN=$(az container show --resource-group $RESOURCE_GROUP --name aci-db-mottu-spot --query ipAddress.fqdn -o tsv)

# Obter credenciais do ACR
ACR_USERNAME=$(az acr credential show -n $ACR_NAME --query username -o tsv)
ACR_PASSWORD=$(az acr credential show -n $ACR_NAME --query "passwords[0].value" -o tsv)

# Configurações
DB_NAME=mottuspot_db
DB_USER=postgres
DB_PASSWORD=postgres123
JAVA_OPTS="-Xmx512m -Xms256m -Dspring.profiles.active=prod"

# Recriar o container da aplicação
echo "🚀 Recriando container da aplicação..."
az container create \
  --resource-group $RESOURCE_GROUP \
  --name aci-app-mottu-spot \
  --image $ACR_NAME.azurecr.io/$APP_IMAGE_NAME:$APP_TAG \
  --cpu 1 --memory 1.5 \
  --registry-login-server $ACR_NAME.azurecr.io \
  --registry-username $ACR_USERNAME \
  --registry-password $ACR_PASSWORD \
  --environment-variables \
    SPRING_DATASOURCE_URL="jdbc:postgresql://$DB_FQDN:5432/$DB_NAME" \
    SPRING_DATASOURCE_USERNAME=$DB_USER \
    SPRING_DATASOURCE_PASSWORD=$DB_PASSWORD \
    SPRING_PROFILES_ACTIVE=prod \
    JAVA_OPTS="$JAVA_OPTS" \
    SPRING_FLYWAY_ENABLED=true \
    SPRING_FLYWAY_BASELINE_ON_MIGRATE=true \
    SPRING_JPA_HIBERNATE_DDL_AUTO=none \
    SPRING_JPA_SHOW_SQL=false \
    SPRING_JPA_OPEN_IN_VIEW=true \
    SPRING_JPA_PROPERTIES_HIBERNATE_ENABLE_LAZY_LOAD_NO_TRANS=true \
  --ports 8080 \
  --os-type Linux \
  --dns-name-label aci-app-mottu-spot \
  --restart-policy Always

# Aguardar a aplicação iniciar
echo "⏳ Aguardando a aplicação iniciar..."
sleep 45

# Verificar se a aplicação está rodando
APP_FQDN=$(az container show --resource-group $RESOURCE_GROUP --name aci-app-mottu-spot --query ipAddress.fqdn -o tsv)

echo ""
echo "✅ Redeploy concluído!"
echo "🚀 Aplicação: http://$APP_FQDN:8080"
echo "🔍 Health: http://$APP_FQDN:8080/actuator/health"
echo "🔑 Login: admin / admin123"
echo "📋 Logs: az container logs --resource-group $RESOURCE_GROUP --name aci-app-mottu-spot"
echo ""
echo "📊 Para verificar status:"
echo "  ./scripts/debug.sh"
echo ""
echo "🔄 Para acompanhar logs em tempo real:"
echo "  az container logs --resource-group $RESOURCE_GROUP --name aci-app-mottu-spot --follow"
