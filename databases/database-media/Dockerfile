FROM mysql:8.0

# Ces variables doivent être définies dans Render en tant qu'environnement
ENV MYSQL_DATABASE=media_db
ENV MYSQL_USER=media_user
ENV MYSQL_PASSWORD=media_password
ENV MYSQL_ROOT_PASSWORD=media_password

VOLUME ["/var/lib/mysql"]

EXPOSE 3306
