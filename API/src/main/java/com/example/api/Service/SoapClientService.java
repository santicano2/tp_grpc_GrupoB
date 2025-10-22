package com.example.api.service;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import jakarta.xml.soap.SOAPElement;
import jakarta.xml.soap.SOAPHeader;
import jakarta.xml.ws.BindingProvider;
import jakarta.xml.ws.handler.Handler;
import jakarta.xml.ws.handler.MessageContext;
import jakarta.xml.ws.handler.soap.SOAPHandler;
import jakarta.xml.ws.handler.soap.SOAPMessageContext;

import org.springframework.stereotype.Service;

import com.example.api.soap.generated.Application;
import com.example.api.soap.generated.OrganizationType;
import com.example.api.soap.generated.OrganizationTypeArray;
import com.example.api.soap.generated.PresidentType;
import com.example.api.soap.generated.PresidentTypeArray;
import com.example.api.soap.generated.SoapApi;
import com.example.api.soap.generated.StringArray;

@Service
public class SoapClientService {

    private static final String WSDL_URL = "https://soap-app-latest.onrender.com/?wsdl";
    private static final String NAMESPACE_URI = "soap.backend";
    private static final String SERVICE_NAME = "SoapApi";
    
    // credenciales de autenticacion SOAP (de la coleccion de Postman)
    private static final String AUTH_GRUPO = "GrupoA-TM";
    private static final String AUTH_CLAVE = "clave-tm-a";

    private Application getPort() throws Exception {
        URL url = new URL(WSDL_URL);
        QName qname = new QName(NAMESPACE_URI, SERVICE_NAME);
        SoapApi service = new SoapApi(url, qname);
        Application port = service.getApplication();
        
        // agregar el handler de autenticacion
        BindingProvider bp = (BindingProvider) port;
        @SuppressWarnings("rawtypes")
        List<Handler> handlerChain = bp.getBinding().getHandlerChain();
        handlerChain.add(new AuthenticationHandler());
        bp.getBinding().setHandlerChain(handlerChain);
        
        return port;
    }
    
    // Handler para agregar headers de autenticacion
    private static class AuthenticationHandler implements SOAPHandler<SOAPMessageContext> {
        
        @Override
        public boolean handleMessage(SOAPMessageContext context) {
            Boolean outboundProperty = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
            
            if (outboundProperty) {
                try {
                    SOAPHeader header = context.getMessage().getSOAPHeader();
                    if (header == null) {
                        header = context.getMessage().getSOAPPart().getEnvelope().addHeader();
                    }
                    
                    // Agregar el elemento auth con namespace
                    QName authQName = new QName("auth.headers", "Auth", "auth");
                    SOAPElement authElement = header.addChildElement(authQName);
                    
                    // agregar Grupo
                    SOAPElement grupoElement = authElement.addChildElement("Grupo", "auth");
                    grupoElement.addTextNode(AUTH_GRUPO);
                    
                    // agregar Clave
                    SOAPElement claveElement = authElement.addChildElement("Clave", "auth");
                    claveElement.addTextNode(AUTH_CLAVE);
                    
                    // guardar los cambios en el mensaje
                    context.getMessage().saveChanges();
                    
                } catch (Exception e) {
                    System.err.println("Error al agregar headers de autenticacion: " + e.getMessage());
                    e.printStackTrace();
                    return false;
                }
            }
            return true;
        }
        
        @Override
        public boolean handleFault(SOAPMessageContext context) {
            return true;
        }
        
        @Override
        public void close(MessageContext context) {
        }
        
        @Override
        public java.util.Set<QName> getHeaders() {
            return null;
        }
    }

    public List<PresidentType> obtenerPresidentes(List<String> orgIds) {
        try {
            Application port = getPort();

            // crear el array de strings
            StringArray stringArray = new StringArray();
            stringArray.getString().addAll(orgIds);

            // llamar directamente al servicio con el StringArray
            PresidentTypeArray result = port.listPresidents(stringArray);

            // procesar el resultado
            if (result != null && result.getPresidentType() != null) {
                return result.getPresidentType();
            }

            return new ArrayList<>();

        } catch (Exception e) {
            System.err.println("Error al obtener presidentes: " + e.getMessage());
            System.err.println("Tipo de excepción: " + e.getClass().getName());
            
            // verificar si es un error de transporte (500, timeout, etc)
            if (e.getMessage() != null && (e.getMessage().contains("500") || 
                e.getMessage().contains("timeout") || 
                e.getMessage().contains("Connection refused"))) {
                System.err.println("El servicio SOAP externo puede estar caído o en modo sleep (Render free tier)");
                throw new RuntimeException("El servicio SOAP externo no está disponible. El servidor remoto devolvió error 500. " +
                    "Si esta alojado en Render (free tier), puede tardar 30 segundos en despertar tras inactividad.", e);
            }
            
            e.printStackTrace();
            throw new RuntimeException("Error al consultar presidentes del servicio SOAP: " + e.getMessage(), e);
        }
    }

    public List<OrganizationType> obtenerOrganizaciones(List<String> orgIds) {
        try {
            Application port = getPort();

            // Crear el array de strings
            StringArray stringArray = new StringArray();
            stringArray.getString().addAll(orgIds);

            // Llamar directamente al servicio con el StringArray
            OrganizationTypeArray result = port.listAssociations(stringArray);

            // Procesar el resultado
            if (result != null && result.getOrganizationType() != null) {
                return result.getOrganizationType();
            }

            return new ArrayList<>();

        } catch (Exception e) {
            System.err.println("Error al obtener organizaciones: " + e.getMessage());
            System.err.println("Tipo de excepción: " + e.getClass().getName());
            
            // verificar si es un error de transporte (500, timeout, etc)
            if (e.getMessage() != null && (e.getMessage().contains("500") || 
                e.getMessage().contains("timeout") || 
                e.getMessage().contains("Connection refused"))) {
                System.err.println("El servicio SOAP externo puede estar caído o en modo sleep (Render free tier)");
                throw new RuntimeException("El servicio SOAP externo no está disponible. El servidor remoto devolvió error 500. " +
                    "Si esta alojado en Render (free tier), puede tardar 30 segundos en despertar tras inactividad.", e);
            }
            
            e.printStackTrace();
            throw new RuntimeException("Error al consultar organizaciones del servicio SOAP: " + e.getMessage(), e);
        }
    }
}