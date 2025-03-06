# Usa Node.js para ejecutar Angular
FROM node:20
WORKDIR /app

# Copia los archivos del frontend
COPY frontend /app

# Instalar dependencias
RUN npm install

# Exponer el puerto de Angular
EXPOSE 4200

# Comando para iniciar la app
CMD ["npm", "start"]
