# ---- ResumeAnalyzer UI (React + Nginx) ----
FROM node:20-alpine AS build
WORKDIR /app

# Ensure PATH includes node_modules/.bin
ENV PATH="./resumeanalyzer-ui/node_modules/.bin:$PATH"

COPY . .
RUN npm install && npm run build

FROM nginx:alpine AS production
COPY --from=build /app/dist /usr/share/nginx/html
COPY nginx.conf.template /etc/nginx/templates/nginx.conf.template
EXPOSE 80
# Use envsubst with variable whitelist to avoid replacing nginx variables
CMD ["/bin/sh", "-c", "envsubst '${BACKEND_HOST}' < /etc/nginx/templates/nginx.conf.template > /etc/nginx/nginx.conf && exec nginx -g 'daemon off;'"]
