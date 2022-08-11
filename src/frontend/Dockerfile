FROM node:latest
WORKDIR /app
COPY package.json ./
RUN npm install --save --legacy-peer-deps
COPY .env /app/.env
COPY . .
CMD ["npm", "start"]
