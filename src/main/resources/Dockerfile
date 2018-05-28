FROM microsoft/mssql-server-windows-express

ENV sa_password=Spoogy21!
ENV ACCEPT_EULA=Y

EXPOSE 1433

RUN sqlcmd -Q \"CREATE DATABASE hoard;\"

CMD .\start -sa_password $env:sa_password -ACCEPT_EULA $env:ACCEPT_EULA -Verbose
