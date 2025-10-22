package com.example.api.service;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;

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

    private Application getPort() throws Exception {
        URL url = new URL(WSDL_URL);
        QName qname = new QName(NAMESPACE_URI, SERVICE_NAME);
        SoapApi service = new SoapApi(url, qname);
        return service.getApplication();
    }

    public List<PresidentType> obtenerPresidentes(List<String> orgIds) {
        try {
            Application port = getPort();

            // Crear el array de strings
            StringArray stringArray = new StringArray();
            stringArray.getString().addAll(orgIds);

            // Llamar directamente al servicio con el StringArray
            PresidentTypeArray result = port.listPresidents(stringArray);

            // Procesar el resultado
            if (result != null && result.getPresidentType() != null) {
                return result.getPresidentType();
            }

            return new ArrayList<>();

        } catch (Exception e) {
            System.err.println("Error al obtener presidentes: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al consultar presidentes del servicio SOAP", e);
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
            e.printStackTrace();
            throw new RuntimeException("Error al consultar organizaciones del servicio SOAP", e);
        }
    }
}