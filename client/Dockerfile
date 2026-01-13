FROM node:latest as builder

RUN mkdir -p /app
WORKDIR /app

COPY package.json /app

RUN npm install

COPY . /app

RUN npm run build --prod

FROM nginx:latest

COPY --from=builder /app/dist/client/browser /usr/share/nginx/html