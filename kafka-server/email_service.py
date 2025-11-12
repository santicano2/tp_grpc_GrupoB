"""
Servicio de envío de emails
"""
import smtplib
import os
import logging
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)


class EmailService:
    def __init__(self):
        # Configuración SMTP desde variables de entorno
        self.smtp_host = os.getenv('SMTP_HOST', 'smtp.gmail.com')
        self.smtp_port = int(os.getenv('SMTP_PORT', '587'))
        self.smtp_user = os.getenv('SMTP_USER', '')
        self.smtp_password = os.getenv('SMTP_PASSWORD', '')
        self.from_email = os.getenv('FROM_EMAIL', self.smtp_user)
        self.from_name = os.getenv('FROM_NAME', 'Empuje Comunitario')
        
        # Flag para desarrollo/testing
        self.debug_mode = os.getenv('EMAIL_DEBUG_MODE', 'true').lower() == 'true'
        
        if self.debug_mode:
            logger.info("Email service en modo DEBUG - los emails se loguearan pero no se enviarán")
        else:
            logger.info(f"Email service configurado: {self.smtp_host}:{self.smtp_port}")
    
    def send_rejection_email(self, email: str, nombre: str, apellido: str, motivo: str) -> bool:
        """
        Envía un email al aspirante rechazado informando el motivo
        
        Args:
            email: Email del aspirante
            nombre: Nombre del aspirante
            apellido: Apellido del aspirante
            motivo: Motivo del rechazo
            
        Returns:
            bool: True si el email se envió correctamente, False en caso contrario
        """
        try:
            subject = "Actualización sobre tu solicitud - Empuje Comunitario"
            
            # Construir el cuerpo del email en HTML
            html_body = self._build_rejection_email_html(nombre, apellido, motivo)
            
            # Construir el cuerpo del email en texto plano (fallback)
            text_body = self._build_rejection_email_text(nombre, apellido, motivo)
            
            # Si estamos en modo debug, solo loguear
            if self.debug_mode:
                logger.info("=" * 60)
                logger.info("📧 EMAIL DEBUG MODE - No se enviará realmente")
                logger.info(f"Para: {email}")
                logger.info(f"Asunto: {subject}")
                logger.info(f"Contenido:\n{text_body}")
                logger.info("=" * 60)
                return True
            
            # Crear el mensaje
            message = MIMEMultipart('alternative')
            message['Subject'] = subject
            message['From'] = f"{self.from_name} <{self.from_email}>"
            message['To'] = email
            
            # Adjuntar ambas versiones
            part1 = MIMEText(text_body, 'plain', 'utf-8')
            part2 = MIMEText(html_body, 'html', 'utf-8')
            message.attach(part1)
            message.attach(part2)
            
            # Enviar el email
            with smtplib.SMTP(self.smtp_host, self.smtp_port) as server:
                # Solo usar STARTTLS y login si hay credenciales configuradas
                # MailHog no requiere autenticación
                if self.smtp_user and self.smtp_password:
                    server.starttls()
                    server.login(self.smtp_user, self.smtp_password)
                server.send_message(message)
            
            logger.info(f"✅ Email de rechazo enviado exitosamente a {email}")
            return True
            
        except Exception as e:
            logger.error(f"❌ Error enviando email a {email}: {str(e)}")
            return False
    
    def _build_rejection_email_html(self, nombre: str, apellido: str, motivo: str) -> str:
        """Construye el cuerpo del email en HTML"""
        return f"""
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <style>
        body {{
            font-family: Arial, sans-serif;
            line-height: 1.6;
            color: #333;
        }}
        .container {{
            max-width: 600px;
            margin: 0 auto;
            padding: 20px;
        }}
        .header {{
            background-color: #2563eb;
            color: white;
            padding: 20px;
            text-align: center;
            border-radius: 8px 8px 0 0;
        }}
        .content {{
            background-color: #f9fafb;
            padding: 30px;
            border-radius: 0 0 8px 8px;
        }}
        .motivo-box {{
            background-color: white;
            border-left: 4px solid #2563eb;
            padding: 15px;
            margin: 20px 0;
            border-radius: 4px;
        }}
        .footer {{
            text-align: center;
            margin-top: 20px;
            color: #6b7280;
            font-size: 14px;
        }}
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>Empuje Comunitario</h1>
        </div>
        <div class="content">
            <h2>Hola {nombre} {apellido},</h2>
            
            <p>Gracias por tu interés en formar parte de <strong>Empuje Comunitario</strong>.</p>
            
            <p>Después de revisar tu solicitud, lamentamos informarte que en este momento no podremos continuar con tu proceso de incorporación.</p>
            
            <div class="motivo-box">
                <strong>Motivo:</strong>
                <p>{motivo}</p>
            </div>
            
            <p>Te agradecemos el tiempo que dedicaste a completar el formulario y tu interés en nuestra organización.</p>
            
            <p>Te invitamos a seguir conectado con nosotros a través de nuestros canales de comunicación, ya que podrías participar en futuras convocatorias.</p>
            
            <p>Saludos cordiales,</p>
            <p><strong>El equipo de Empuje Comunitario</strong></p>
        </div>
        <div class="footer">
            <p>Este es un correo automático, por favor no responder.</p>
        </div>
    </div>
</body>
</html>
        """
    
    def _build_rejection_email_text(self, nombre: str, apellido: str, motivo: str) -> str:
        """Construye el cuerpo del email en texto plano"""
        return f"""
Hola {nombre} {apellido},

Gracias por tu interés en formar parte de Empuje Comunitario.

Después de revisar tu solicitud, lamentamos informarte que en este momento no podremos continuar con tu proceso de incorporación.

MOTIVO:
{motivo}

Te agradecemos el tiempo que dedicaste a completar el formulario y tu interés en nuestra organización.

Te invitamos a seguir conectado con nosotros a través de nuestros canales de comunicación, ya que podrías participar en futuras convocatorias.

Saludos cordiales,
El equipo de Empuje Comunitario

---
Este es un correo automático, por favor no responder.
        """


# Instancia global del servicio
email_service = EmailService()