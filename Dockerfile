FROM cwds/javajdk
RUN mkdir /opt/template-api
RUN mkdir /opt/template-api/logs
ADD config/*.yml /opt/template-api/
ADD config/shiro.ini /opt/template-api/config/shiro.ini
ADD config/shiro_nosec.ini /opt/template-api/config/shiro_nosec.ini
ADD config/testKeyStore.jks /opt/template-api/config/testKeyStore.jks
ADD build/libs/template-api-dist.jar /opt/template-api/template-api.jar
ADD build/entrypoint.sh /opt/template-api/
EXPOSE 8080 
RUN chmod +x /opt/template-api/entrypoint.sh
WORKDIR /opt/template-api
CMD ["/opt/template-api/entrypoint.sh"]