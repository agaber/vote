# See https://blog.mayadata.io/openebs/steps-to-deploy-angular-application-on-kubernetes

# Stage 1: Build and compile the frontend with Node.js.
FROM node:18.14.1 as build-stage
WORKDIR /app
COPY package*.json /app/
RUN npm install
COPY ./ /app/
ARG configuration=production
RUN npm run build -- --output-path=./dist/out --configuration $configuration

# Stage 2: Have the compiled app ready for production with Nginx.
FROM nginx:1.22.1
COPY --from=build-stage /app/dist/out/ /usr/share/nginx/html
COPY ./nginx.conf /etc/nginx/conf.d/default.conf
