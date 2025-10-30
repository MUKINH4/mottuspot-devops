#!/bin/bash

# Variáveis
RESOURCE_GROUP=rg-mottu-spot
ACR_NAME=mottuspot
APP_IMAGE_NAME=mottu-spot
DB_IMAGE_NAME=postgres
POSTGRES_VERSION=17-alpine
DB_TAG=17-alpine
APP_TAG=latest

# Configurações do banco
DB_NAME=mottuspot_db
DB_USER=postgres
DB_PASSWORD=postgres123

# Configurações da aplicação
JAVA_OPTS="-Xmx512m -Xms256m -Dspring.profiles.active=prod"

# Obter credenciais do ACR
echo "🔑 Obtendo credenciais do ACR..."
ACR_USERNAME=$(az acr credential show -n $ACR_NAME --query username -o tsv)
ACR_PASSWORD=$(az acr credential show -n $ACR_NAME --query "passwords[0].value" -o tsv)

# Obtém o FQDN correto do banco
DB_FQDN=$(az container show --resource-group $RESOURCE_GROUP --name aci-db-mottu-spot --query ipAddress.fqdn -o tsv)
echo "✅ Banco de dados disponível em: $DB_FQDN:5432"

# Deploy da aplicação
echo "🚀 Criando container da aplicação no ACI..."
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
    SPRING_JPA_SHOW_SQL=true \
    SERVER_ERROR_INCLUDE_STACKTRACE=always \
    SERVER_ERROR_INCLUDE_MESSAGE=always \
  --ports 8080 \
  --os-type Linux \
  --dns-name-label aci-app-mottu-spot \
  --restart-policy Always

# Aguardar a aplicação iniciar
echo "⏳ Aguardando a aplicação iniciar..."
sleep 60

# Verificar se a aplicação está rodando
APP_FQDN=$(az container show --resource-group $RESOURCE_GROUP --name aci-app-mottu-spot --query ipAddress.fqdn -o tsv)

echo "✅ Deploy concluído!"
echo "📊 Banco de Dados: $DB_FQDN:5432"
echo "🚀 Aplicação: http://$APP_FQDN:8080"
echo "🔑 Login: admin / admin123"
echo "📋 Para verificar logs: az container logs --resource-group $RESOURCE_GROUP --name aci-app-mottu-spot"
