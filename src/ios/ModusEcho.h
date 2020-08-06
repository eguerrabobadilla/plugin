#import <Cordova/CDV.h>

@interface ModusEcho : CDVPlugin

- (void)echo:(CDVInvokedUrlCommand*)command;

- (void)Insertar:(CDVInvokedUrlCommand*)command;

- (void)CambioHoja:(CDVInvokedUrlCommand*)command;

- (void)AbrirNota:(CDVInvokedUrlCommand*)command;

- (void)TotalHojas:(CDVInvokedUrlCommand*)command;

- (void)GuardarRayado:(CDVInvokedUrlCommand*)command;

- (void)GuardarSubrayado:(CDVInvokedUrlCommand*)command;

- (void)InsertarEscribir:(CDVInvokedUrlCommand*)command;

- (void)CerrarEjercicios:(CDVInvokedUrlCommand*)command;

- (void)dibujarEjercicios:(CDVInvokedUrlCommand*)command;

- (void)calificarEjercicios:(CDVInvokedUrlCommand*)command;

- (void)TomarFoto:(CDVInvokedUrlCommand*)command;

- (void)cambiarCandado:(CDVInvokedUrlCommand*)command;

- (void)EliminarFoto:(CDVInvokedUrlCommand*)command;

@end
