#import "ModusEcho.h"
#import <Cordova/CDV.h>
#import "ViewController.h"
#import "SQLitePlugin.h"
#import "sqlite3.h"

@implementation ModusEcho
ViewController * yourViewController;

- (void)CambioHoja:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* pluginResult = nil;
    NSString* msg = [command.arguments objectAtIndex:0];
    
    yourViewController.HojaActual=msg;
    [yourViewController CambioHoja];
}
- (void)AbrirNota:(CDVInvokedUrlCommand*)command;
{
    CDVPluginResult* pluginResult = nil;
    NSString* msg = [command.arguments objectAtIndex:0];
    
    if (msg == nil || [msg length] == 0) {
        [yourViewController AbrirNota];
    }
    else
    {
        yourViewController.DatosNota=msg;
        [yourViewController AbrirNota];
    }

}
- (void)ShowIndice:(CDVInvokedUrlCommand*)command;
{
    CDVPluginResult* pluginResult = nil;
    NSString* msg = [command.arguments objectAtIndex:0];
    
    if (msg == nil || [msg length] == 0) {
        [yourViewController AbrirIndice];
    }
    else
    {
        yourViewController.Indice=command.arguments;
        [yourViewController AbrirIndice];
    }
    
}
- (void)ShowImagenes:(CDVInvokedUrlCommand*)command;
{
    CDVPluginResult* pluginResult = nil;
    
    if(command.arguments.count==0)
    {
        [yourViewController AbrirGaleria];
        return;
    }
    
    NSString* msg = [command.arguments objectAtIndex:0];
    
    if (msg == nil || [msg length] == 0) {
        [yourViewController AbrirGaleria];
    }
    else
    {
        yourViewController.ImagenesGaleria=command.arguments;
        [yourViewController AbrirGaleria];
    }
    
}
- (void)TotalHojas:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* pluginResult = nil;
    NSString *msg = [command.arguments objectAtIndex:0];
    
    yourViewController.TotalPaginas=[msg integerValue];
}
- (void)GuardarRayado:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* pluginResult = nil;
    NSString *Hoja = [command.arguments objectAtIndex:0];
    NSString *Data = [command.arguments objectAtIndex:1];
    
    const char *dbpath = [yourViewController.databasePath UTF8String];
    sqlite3_stmt *statement;
    
    sqlite3 *db=yourViewController.db;
    if (sqlite3_open(dbpath, &db) == SQLITE_OK)
    {
            NSString *querySQL = [NSString stringWithFormat:
                                  @"SELECT DATA FROM RAYADO WHERE LIBROID=\"%@\" AND HOJA=\"%@\"",
                                  yourViewController.LibroId,Hoja];
            NSString *sql;
            const char *query_stmt = [querySQL UTF8String];
            
            if (sqlite3_prepare_v2(db,query_stmt, -1, &statement, NULL) == SQLITE_OK)
            {
                if(sqlite3_step(statement) == SQLITE_ROW)
                {
                    sql = [NSString stringWithFormat:
                                     //@"INSERT INTO RAYADO (LIBROID,HOJA,DATA) VALUES (\"%@\", \"%@\",\"%@\")",
                                     @"UPDATE RAYADO SET DATA='%@' WHERE LIBROID='%@' AND HOJA='%@'",
                                     Data,yourViewController.LibroId,Hoja];
                    sqlite3_finalize(statement);
                    sqlite3_close(db);
                    //[self saveData:sql];
                }
                else
                {
                    sql = [NSString stringWithFormat:
                                     //@"INSERT INTO RAYADO (LIBROID,HOJA,DATA) VALUES (\"%@\", \"%@\",\"%@\")",
                                     @"INSERT INTO RAYADO (LIBROID,HOJA,DATA) VALUES ('%@', '%@','%@')",
                                     yourViewController.LibroId,Hoja,Data];
                    //[self saveData:sql];
                    sqlite3_finalize(statement);
                    sqlite3_close(db);
                }
                [yourViewController saveData:(sql)];
            }
    }
}
- (void)GuardarSubrayado:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* pluginResult = nil;
    NSString *Hoja = [command.arguments objectAtIndex:0];
    NSString *Data = [command.arguments objectAtIndex:1];
    
    const char *dbpath = [yourViewController.databasePath UTF8String];
    sqlite3_stmt *statement;
    
    sqlite3 *db=yourViewController.db;
    if (sqlite3_open(dbpath, &db) == SQLITE_OK)
    {
        NSString *querySQL = [NSString stringWithFormat:
                              @"SELECT DATA FROM SUBRAYADO WHERE LIBROID=\"%@\" AND HOJA=\"%@\"",
                              yourViewController.LibroId,Hoja];
        NSString *sql;
        const char *query_stmt = [querySQL UTF8String];
        
        if (sqlite3_prepare_v2(db,query_stmt, -1, &statement, NULL) == SQLITE_OK)
        {
            if(sqlite3_step(statement) == SQLITE_ROW)
            {
                sql = [NSString stringWithFormat:
                       //@"INSERT INTO RAYADO (LIBROID,HOJA,DATA) VALUES (\"%@\", \"%@\",\"%@\")",
                       @"UPDATE SUBRAYADO SET DATA='%@' WHERE LIBROID='%@' AND HOJA='%@'",
                       Data,yourViewController.LibroId,Hoja];
                sqlite3_finalize(statement);
                sqlite3_close(db);
                //[self saveData:sql];
            }
            else
            {
                sql = [NSString stringWithFormat:
                       //@"INSERT INTO RAYADO (LIBROID,HOJA,DATA) VALUES (\"%@\", \"%@\",\"%@\")",
                       @"INSERT INTO SUBRAYADO (LIBROID,HOJA,DATA) VALUES ('%@', '%@','%@')",
                       yourViewController.LibroId,Hoja,Data];
                //[self saveData:sql];
                sqlite3_finalize(statement);
                sqlite3_close(db);
            }
            [yourViewController saveData:(sql)];
        }
    }
}
- (void)InsertarEscribir:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* pluginResult = nil;
    NSString *Ejercicio = [command.arguments objectAtIndex:0];
    NSString *Data = [command.arguments objectAtIndex:1];
    NSString *Elemento = [command.arguments objectAtIndex:2];
    
    const char *dbpath = [yourViewController.databasePath UTF8String];
    sqlite3_stmt *statement;
    
    sqlite3 *db=yourViewController.db;
    if (sqlite3_open(dbpath, &db) == SQLITE_OK)
    {
        NSString *querySQL = [NSString stringWithFormat:
                              @"SELECT DATA FROM ESCRIBIR WHERE LIBROID=\"%@\" AND EJERCICIO=\"%@\" AND ELEMENTO=\"%@\"",
                              yourViewController.LibroId,Ejercicio,Elemento];
        NSString *sql;
        const char *query_stmt = [querySQL UTF8String];
        
        if (sqlite3_prepare_v2(db,query_stmt, -1, &statement, NULL) == SQLITE_OK)
        {
            if(sqlite3_step(statement) == SQLITE_ROW)
            {
                //Estado preguntas 0=Incorrecta, 1=Correcta, 2=Sin calificar
                sql = [NSString stringWithFormat:
                       //@"INSERT INTO RAYADO (LIBROID,EJERCICIO,DATA) VALUES (\"%@\", \"%@\",\"%@\")",
                       @"UPDATE ESCRIBIR SET DATA='%@' WHERE LIBROID='%@' AND EJERCICIO='%@' AND ELEMENTO='%@'",
                       Data,yourViewController.LibroId,Ejercicio,Elemento];
                sqlite3_finalize(statement);
                sqlite3_close(db);
                //[self saveData:sql];
            }
            else
            {
                //Estado preguntas 0=Incorrecta, 1=Correcta, 2=Sin calificar
                
                sql = [NSString stringWithFormat:
                       //@"INSERT INTO RAYADO (LIBROID,EJERCICIO,DATA) VALUES (\"%@\", \"%@\",\"%@\")",
                       @"INSERT INTO ESCRIBIR (LIBROID,EJERCICIO,DATA,ELEMENTO,ESTADO) VALUES ('%@', '%@','%@','%@',2)",
                       yourViewController.LibroId,Ejercicio,Data,Elemento];
                //[self saveData:sql];
                sqlite3_finalize(statement);
                sqlite3_close(db);
            }
            [yourViewController saveData:(sql)];
        }
    }
}
- (void)calificarEjercicios:(CDVInvokedUrlCommand*)command
{
    //Estado preguntas 0=Incorrecta, 1=Correcta, 2=Sin calificar
    
    CDVPluginResult* pluginResult = nil;
    NSString *Estado = [command.arguments objectAtIndex:0];
    NSString *Ejercicio = [command.arguments objectAtIndex:1];
    NSString *Elemento = [command.arguments objectAtIndex:2];
    
    const char *dbpath = [yourViewController.databasePath UTF8String];
    sqlite3_stmt *statement;
    
    sqlite3 *db=yourViewController.db;
    if (sqlite3_open(dbpath, &db) == SQLITE_OK)
    {
        NSString *querySQL = [NSString stringWithFormat:
                              @"SELECT DATA FROM ESCRIBIR WHERE LIBROID=\"%@\" AND EJERCICIO=\"%@\" AND ELEMENTO=\"%@\"",
                              yourViewController.LibroId,Ejercicio,Elemento];
        NSString *sql;
        const char *query_stmt = [querySQL UTF8String];
        
        if (sqlite3_prepare_v2(db,query_stmt, -1, &statement, NULL) == SQLITE_OK)
        {
            if(sqlite3_step(statement) == SQLITE_ROW)
            {
                //Estado preguntas 0=Incorrecta, 1=Correcta, 2=Sin calificar
                sql = [NSString stringWithFormat:
                       //@"INSERT INTO RAYADO (LIBROID,EJERCICIO,DATA) VALUES (\"%@\", \"%@\",\"%@\")",
                       @"UPDATE ESCRIBIR SET ESTADO='%@' WHERE LIBROID='%@' AND EJERCICIO='%@' AND ELEMENTO='%@'",
                       Estado,yourViewController.LibroId,Ejercicio,Elemento];
                sqlite3_finalize(statement);
                sqlite3_close(db);
                //[self saveData:sql];
            }
            else
            {
                //Estado preguntas 0=Incorrecta, 1=Correcta, 2=Sin calificar
                
                sql = [NSString stringWithFormat:
                       //@"INSERT INTO RAYADO (LIBROID,EJERCICIO,DATA) VALUES (\"%@\", \"%@\",\"%@\")",
                       @"INSERT INTO ESCRIBIR (LIBROID,EJERCICIO,DATA,ELEMENTO,ESTADO) VALUES ('%@', '%@','','%@','%@')",
                       yourViewController.LibroId,Ejercicio,Elemento,Estado];
                //[self saveData:sql];
                sqlite3_finalize(statement);
                sqlite3_close(db);
            }
            [yourViewController saveData:(sql)];
        }
    }
}
- (void)Insertar:(CDVInvokedUrlCommand*)command
{
    [yourViewController ActivarTap];
}
- (void)CerrarEjercicios:(CDVInvokedUrlCommand*)command
{
    [yourViewController ClickEjercicios:nil];
}
- (void)echo:(CDVInvokedUrlCommand*)command
{
  CDVPluginResult* pluginResult = nil;
  NSArray* msg = [command.arguments objectAtIndex:0];
    
  NSString* PathLibro = [msg objectAtIndex:0];
  NSString* LibroId   = [msg objectAtIndex:1];
  NSString* App       = [msg objectAtIndex:2];
  //NSString* LibroId = [NSString stringWithFormat: @"%d", [msg objectAtIndex:1]];
 
  if (msg == nil) {
    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
  } else {
    UIAlertView *toast = [
      [UIAlertView alloc] initWithTitle:@""
        message:msg
        delegate:nil
        cancelButtonTitle:nil
        otherButtonTitles:nil, nil];

    //[toast show];
    //[self.viewController.navigationController pushViewController:viewController animated:YES];
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"Main" bundle:nil];
    yourViewController = [storyboard   instantiateViewControllerWithIdentifier:@"ViewController"];
      
    /***PAth a mis documentos*/
    NSString *docsDir;
    NSArray *dirPaths;
      
    // Get the documents directory
    dirPaths = NSSearchPathForDirectoriesInDomains(NSLibraryDirectory, NSUserDomainMask, YES);
      
    docsDir = dirPaths[0];
      
    docsDir= [[NSString alloc]
                initWithString: [docsDir stringByAppendingPathComponent:@"NoCloud"]];
      
    docsDir= [[NSString alloc]
                initWithString: [docsDir stringByAppendingPathComponent:@"books2019"]];
      
    NSArray *arrayPathLibro = [PathLibro componentsSeparatedByString:@"/"];
    NSString *NombreLibro=arrayPathLibro[[arrayPathLibro count]-1];
      
    NSLog(@"libro = %@",NombreLibro);
    
    // Build the path to the database file
    NSString *pathLibroReal = [[NSString alloc]
                       initWithString: [docsDir stringByAppendingPathComponent:
                                       NombreLibro]];
  
    yourViewController.productURL= [NSString stringWithFormat: @"file://%@", pathLibroReal];
    yourViewController.App = App;
    yourViewController.LibroId=LibroId;
    //yourViewController.LibroId=PathLibro;
    //Linea agregada para ios 13
    yourViewController.modalPresentationStyle = UIModalPresentationCustom;
    [self.viewController presentViewController: yourViewController animated:YES completion:nil];

    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, 3 * NSEC_PER_SEC), 
    dispatch_get_main_queue(), ^{
      [toast dismissWithClickedButtonIndex:0 animated:YES];
    });
        
    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:msg];
  }

  [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)dibujarEjercicios:(CDVInvokedUrlCommand*)command{
    NSString* ejercicio = [command.arguments objectAtIndex:0];
    
   // NSString* ejercicio = [msg objectAtIndex:0];

    [yourViewController dibujarEscribir: ejercicio];
}
- (void)TomarFoto:(CDVInvokedUrlCommand*)command{
    NSString* IdImg = [command.arguments objectAtIndex:0];
    
    // NSString* ejercicio = [msg objectAtIndex:0];
    
    [yourViewController TomarFoto: IdImg];
}
- (void)cambiarCandado:(CDVInvokedUrlCommand*)command {
    NSString* estado = [command.arguments objectAtIndex:0];
    
    // NSString* ejercicio = [msg objectAtIndex:0];
    
    [yourViewController cambiarCandado: estado];
}
- (void)EliminarFoto:(CDVInvokedUrlCommand*)command{
    NSString* IdImg = [command.arguments objectAtIndex:0];
    
     [yourViewController EliminarFoto: IdImg];
}
@end
