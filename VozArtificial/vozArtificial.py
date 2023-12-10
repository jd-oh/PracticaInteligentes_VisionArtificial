import pyttsx3

# Crear un objeto de motor de texto a voz
engine = pyttsx3.init()

# Texto que deseas convertir en voz
col=1
fil=2
mensaje = f"El siguiente movimiento es en la columna numero {col} y en la fila numero {fil}"

# Configura la voz y el idioma (puedes ajustar la voz según tus preferencias)
voices = engine.getProperty('voices')
engine.setProperty('voice', voices[0].id)  # Elige la primera voz disponible

# Reproduce el mensaje
engine.say(mensaje)
engine.runAndWait()

# Cierra el motor de texto a voz
engine.stop()
