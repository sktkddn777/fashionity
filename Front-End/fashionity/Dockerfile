FROM node:14 as build-stage
ENV TZ=Asia/Seoul
WORKDIR /app

COPY package*.json ./
RUN npm install
COPY . .

EXPOSE 3333
CMD ["npm","run","serve"]