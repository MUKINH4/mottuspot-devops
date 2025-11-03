#!/bin/bash

ACR_NAME="mottuspot"
RG_NAME="rg-mottu-spot"
IMAGE_NAME="mottu-spot"
DB_NAME="mottuspot"
DB_USER="postgres"
DB_PASSWORD="postgres123"
LOCATION="eastus"

# Criar Resource Group
echo "Criando Resource Group..."
az group create --name $RG_NAME --location eastus

# Criar Azure Container Registry
echo "Criando Azure Container Registry..."
az acr create --resource-group $RG_NAME --name $ACR_NAME --sku Basic --admin-enabled true

# Fazer login no ACR
echo "Fazendo login no ACR..."
az acr login --name $ACR_NAME

docker pull postgres:17-alpine
docker tag postgres:17-alpine $ACR_NAME.azurecr.io/postgres:17-alpine
docker push $ACR_NAME.azurecr.io/postgres:17-alpine

ACR_USERNAME=$(az acr credential show -n "$ACR_NAME" --query username -o tsv)
ACR_PASSWORD=$(az acr credential show -n "$ACR_NAME" --query "passwords[0].value" -o tsv)

# Nomes do container e DNS do banco
DB_CONTAINER_NAME="aci-db-mottu-spot"
DB_DNS_LABEL="aci-db-mottu-spot"

# Excluir container anterior se existir
echo "Limpando container anterior do banco (se existir)..."
az container delete --resource-group "$RG_NAME" --name "$DB_CONTAINER_NAME" --yes 2>/dev/null || true
sleep 5

# Criar container do banco
echo "Criando container do banco: $DB_CONTAINER_NAME"
az container create \
  --resource-group "$RG_NAME" \
  --name "$DB_CONTAINER_NAME" \
  --image "${ACR_NAME}.azurecr.io/postgres:17-alpine" \
  --cpu 1 --memory 2 \
  --registry-login-server "${ACR_NAME}.azurecr.io" \
  --registry-username "$ACR_USERNAME" \
  --registry-password "$ACR_PASSWORD" \
  --environment-variables \
    POSTGRES_PASSWORD="$DB_PASSWORD" \
    POSTGRES_DB="$DB_NAME" \
    POSTGRES_USER="$DB_USER" \
  --ports 5432 \
  --os-type Linux \
  --dns-name-label "$DB_DNS_LABEL" \
  --location "$LOCATION" \
  --restart-policy Always

# Obter FQDN do banco
DB_FQDN=$(az container show --resource-group "$RG_NAME" --name "$DB_CONTAINER_NAME" --query ipAddress.fqdn -o tsv)

echo ""
echo "✅ Banco de dados criado com sucesso!"
echo "URL do banco: jdbc:postgresql://${DB_FQDN}:5432/${DB_NAME}"
echo "Usuário: ${DB_USER}"
echo "Senha: ${DB_PASSWORD}"
echo ""
echo "Configure a variável SPRING_DATASOURCE_URL no Azure DevOps:"
echo "jdbc:postgresql://${DB_FQDN}:5432/${DB_NAME}"