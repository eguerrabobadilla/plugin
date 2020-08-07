package com.moduscreate.plugin;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.icu.text.SymbolTable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.sax.Element;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.os.Environment;

import org.apache.commons.io.IOUtils;
import org.apache.cordova.CordovaActivity;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebViewImpl;
import org.apache.cordova.engine.SystemWebView;
import org.apache.cordova.engine.SystemWebViewClient;
import org.apache.cordova.engine.SystemWebViewEngine;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.webkit.JavascriptInterface;

import java.io.File;
import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import lbs.starter.BuildConfig;
import lbs.starter.MainActivity;
import me.echodev.resizer.Resizer;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;


public class NewActivity extends CordovaActivity {
    SystemWebView web1;
    ImageButton FabMain,FabBookmark,FabFavoritos,FabIndice,FabNotas,FabRayar,FabSubrayar,FabFoto,FabNumeroPagina,FabCalificar,FabCalificarOpen;
    ImageButton FabSubrayarAmarillo,FabSubrayarRosa,FabSubrayarAzul;
    ImageButton FabRayarNegro,FabRayarRojo,FabRayarAmarillo,FabRayarVerde,FabRayarRosa,FabRayarAzul,FabRayarColor,FabRayarRosaLigero,FabRayarGrey,FabRayarMarron;
    ImageButton FabBorrar,FabBorrarActivo,FabCloseSubrayar,FabColorSubrayado;
    Button FabTamano;
    ImageButton FabUndo,FabLimpiar,FabCloseRayar;
    ImageButton FabEjercicios;
    TextView txtNumeroPagina;
    ImageButton FabTomarFoto,FabGaleria,FabCloseFoto;
    Boolean isFabOpen=false,FotoActivo=false,RayarActivo=false,SubRayar=false,TapActivo=false,BorrarActivo=false,FavoritosActivo = false;
    LinearLayout PanelSubrayado;
    TableLayout  PanelRayado;
    String  HojaActual,nombrearchivo;
    int libroid,cargando = 0,TotalPaginas = 0;
    EditText e2;
    Dialog ThisDialog,ThisDialogGrid;
    Button guardar;
    Button borrar;
    Button btnSeleccionarImagen;
    ImageButton btnBorrarImagen;
    ArrayList<String> f = new ArrayList<String>();
    ArrayList<String> borrargaleria = new ArrayList<String>();
    List<File> fileList = new ArrayList<File>();
    File[] listFile;
    File[] listFileBorrar;
    Integer[] imageIDs = {};
    GridView androidGridView;
    SQLiteDatabase bd;
    String Capitulos;
    int tipofoto;
    String imagenid;
    String paginaid;
    SeekBar simpleSeekBar;
    int progresvisible=1;
    double division=Math.pow(0.0, 1);

    private static final String GOOGLE_PHOTOS_PACKAGE_NAME = "com.google.android.apps.photos";
  private static final int CAMERA_REQUEST = 1888;
  @Override
    protected CordovaWebView makeWebView() {
      //SystemWebView webView =SystemWebView)findViewById(R.id.cordovaWebView);
      String package_name = getApplication().getPackageName();
      SystemWebView webView =web1=(SystemWebView)findViewById(getApplication().getResources().getIdentifier("web1", "id", package_name));
      //eduardo
      webView.setInitialScale(0);
      webView.setVerticalScrollBarEnabled(false);
      webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
      webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
      web1.clearCache(true);
      web1.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
      web1.getSettings().setAppCacheEnabled(false);
      webView.getSettings().setUseWideViewPort(true);


      webView.getSettings().setJavaScriptEnabled(true);
      webView.getSettings().setBuiltInZoomControls(true);
      webView.getSettings().setDisplayZoomControls(false);
      webView.getSettings().setAllowFileAccessFromFileURLs(true);
      webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
      webView.getSettings().setAllowFileAccess(true);
      webView.getSettings().setDomStorageEnabled(true);
      webView.addJavascriptInterface(new WebViewJavaScriptInterface(this,this), "app");
      return new CordovaWebViewImpl(new SystemWebViewEngine(webView));
    }

    @Override
    protected void createViews() {
      appView.getView().requestFocusFromTouch();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String package_name = getApplication().getPackageName();
        //String package_name = "com.moduscreate.plugin";
        this.requestWindowFeature(1);


        setContentView(getApplication().getResources().getIdentifier("activity_new", "layout", package_name));
        this.HojaActual = "1";
       this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.ThisDialog = new Dialog(this);
        this.ThisDialog.requestWindowFeature(1);
        this.ThisDialog.setContentView(getApplication().getResources().getIdentifier("modal", "layout", getApplication().getPackageName()));
        //getActionBar().hide();


      //***** inicializar base de datos
      bd = new AdminSQLiteOpenHelper(getApplicationContext(), "libros", null, 1).getWritableDatabase();
      int resId = getResources().getIdentifier("status_bar_height", "dimen", "android");
      if (resId > 0) {
        int result = getResources().getDimensionPixelSize(resId);
      }

        txtNumeroPagina =  (TextView) findViewById(getResources().getIdentifier("txtNumeroPagina", "id", package_name));
        FabNumeroPagina = (ImageButton) findViewById(getResources().getIdentifier("FabNumeroPagina", "id", package_name));
        FabCalificar = (ImageButton) findViewById(getResources().getIdentifier("FabCalificar", "id", package_name));
        FabCalificarOpen = (ImageButton) findViewById(getResources().getIdentifier("FabCalificarOpen", "id", package_name));

        //web1=(SystemWebView)findViewById(getApplication().getResources().getIdentifier("web1", "id", package_name));
        FabMain = (ImageButton) findViewById(getResources().getIdentifier("FabMain", "id", package_name));
        FabBookmark = (ImageButton) findViewById(getResources().getIdentifier("FabBookmark", "id", package_name));
        FabFavoritos = (ImageButton) findViewById(getResources().getIdentifier("FabFavoritos", "id", package_name));
        FabIndice = (ImageButton) findViewById(getResources().getIdentifier("FabIndice", "id", package_name));
        FabNotas = (ImageButton) findViewById(getResources().getIdentifier("FabNotas", "id", package_name));
        FabFoto = (ImageButton) findViewById(getResources().getIdentifier("FabFoto", "id", package_name));
        FabRayar = (ImageButton) findViewById(getResources().getIdentifier("FabRayar", "id", package_name));
        FabSubrayar = (ImageButton) findViewById(getResources().getIdentifier("FabSubrayar", "id", package_name));


        //Botones Subrayado
        FabBorrar = (ImageButton) findViewById(getResources().getIdentifier("FabBorrar", "id", package_name));
        FabBorrarActivo = (ImageButton) findViewById(getResources().getIdentifier("FabBorrarActivo", "id", package_name));
        FabCloseSubrayar = (ImageButton) findViewById(getResources().getIdentifier("FabCloseSubrayar", "id", package_name));

        //Botones Color Subryado
        FabColorSubrayado=(ImageButton) findViewById(getResources().getIdentifier("FabColorSubrayado", "id", package_name));
        FabSubrayarAmarillo=(ImageButton) findViewById(getResources().getIdentifier("FabSubrayarAmarillo", "id", package_name));
        FabSubrayarRosa=(ImageButton) findViewById(getResources().getIdentifier("FabSubrayarRosa", "id", package_name));
        FabSubrayarAzul=(ImageButton) findViewById(getResources().getIdentifier("FabSubrayarAzul", "id", package_name));
        PanelSubrayado=(LinearLayout) findViewById(getResources().getIdentifier("PanelSubrayado", "id", package_name));

        //Botones Rayar
        FabRayarColor=(ImageButton) findViewById(getResources().getIdentifier("FabRayarColor", "id", package_name));
        FabUndo = (ImageButton) findViewById(getResources().getIdentifier("FabUndo", "id", package_name));
        FabLimpiar = (ImageButton) findViewById(getResources().getIdentifier("FabLimpiar", "id", package_name));
        FabCloseRayar = (ImageButton) findViewById(getResources().getIdentifier("FabCloseRayar", "id", package_name));
        FabTamano = (Button) findViewById(getResources().getIdentifier("FabTamano", "id", package_name));
        simpleSeekBar = (SeekBar) findViewById(getResources().getIdentifier("simpleSeekBar", "id", package_name));

        //Botones Color Rayar
        FabRayarNegro= (ImageButton) findViewById(getResources().getIdentifier("FabRayarNegro", "id", package_name));
        FabRayarRojo= (ImageButton) findViewById(getResources().getIdentifier("FabRayarRojo", "id", package_name));
        FabRayarAmarillo= (ImageButton) findViewById(getResources().getIdentifier("FabRayarAmarillo", "id", package_name));
        FabRayarVerde= (ImageButton) findViewById(getResources().getIdentifier("FabRayarVerde", "id", package_name));
        FabRayarRosa= (ImageButton) findViewById(getResources().getIdentifier("FabRayarRosa", "id", package_name));
        FabRayarAzul= (ImageButton) findViewById(getResources().getIdentifier("FabRayarAzul", "id", package_name));
        FabRayarRosaLigero= (ImageButton) findViewById(getResources().getIdentifier("FabRayarRosaLigero", "id", package_name));
        FabRayarGrey= (ImageButton) findViewById(getResources().getIdentifier("FabRayarGrey", "id", package_name));
        FabRayarMarron= (ImageButton) findViewById(getResources().getIdentifier("FabRayarMarron", "id", package_name));
        PanelRayado=(TableLayout ) findViewById(getResources().getIdentifier("PanelRayado", "id", package_name));

        //Botones Foto
        FabTomarFoto = (ImageButton) findViewById(getResources().getIdentifier("FabTomarFoto", "id", package_name));
        FabGaleria = (ImageButton) findViewById(getResources().getIdentifier("FabGaleria", "id", package_name));
        FabCloseFoto = (ImageButton) findViewById(getResources().getIdentifier("FabCloseFoto", "id", package_name));

        //Botones notas
        this.e2 = (EditText) this.ThisDialog.findViewById(getResources().getIdentifier("e2", "id", getApplication().getPackageName()));
        this.guardar = (Button) this.ThisDialog.findViewById(getResources().getIdentifier("guardar", "id", getApplication().getPackageName()));
         this.borrar = (Button) this.ThisDialog.findViewById(getResources().getIdentifier("borrar", "id", getApplication().getPackageName()));

        //Botones Ejercicios
        FabEjercicios = (ImageButton) findViewById(getResources().getIdentifier("FabEjercicios", "id", package_name));

        Bundle bundle1 = getIntent().getExtras();
        String mostrarbotonejercicio=bundle1.getString("app");
        FabEjercicios.setVisibility(View.GONE);



  		FabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFabOpen==false )
                    ShowFabMenu();
                else
                    CloseFabMenu();
            }
        });

        FabCalificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                web1.loadUrl("javascript:" + "Visor.activarCandados(2)");
            }
        });
        FabCalificarOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                web1.loadUrl("javascript:" + "Visor.activarCandados(2)");
                FabCalificarOpen.setVisibility(View.GONE);

            }
        });

        FabBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  if(SubRayar==true || RayarActivo==true) return;
              if(RayarActivo==true)FabCloseRayar.callOnClick();
              if(FotoActivo==true)FabCloseFoto.callOnClick();
              if(SubRayar==true) FabCloseSubrayar.callOnClick();

              String js;
            //  SQLiteDatabase bd = new AdminSQLiteOpenHelper(getApplicationContext(), "libros", null, 1).getWritableDatabase();

              Cursor fila=bd.rawQuery("SELECT * FROM SEPARADORES WHERE HOJA='" + HojaActual + "' and LIBROID=" + libroid, null);
              if (fila.moveToFirst()) {
                bd.delete("SEPARADORES", "HOJA='" + HojaActual + "'", null);
                js = "Visor.borrarSeparador(" + HojaActual + ")";
              } else {
                ContentValues registro = new ContentValues();
                registro.put("LIBROID", Integer.valueOf(libroid));
                registro.put("HOJA", HojaActual);
                bd.insert("SEPARADORES", null, registro);
                js = "Visor.dibujarSeparador(" + HojaActual + ")";
              }
              fila.close();
             // bd.close();
              web1.loadUrl("javascript:" + js);
            }
        });
        FabFavoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  if(SubRayar==true || RayarActivo==true) return;
              if(RayarActivo==true)FabCloseRayar.callOnClick();
              if(FotoActivo==true)FabCloseFoto.callOnClick();
              if(SubRayar==true) FabCloseSubrayar.callOnClick();

              ResetZoom();

              if (!NewActivity.this.SubRayar.booleanValue() && !NewActivity.this.RayarActivo.booleanValue()) {
                NewActivity.this.ResetZoom();
                if (NewActivity.this.FavoritosActivo.booleanValue()) {
                  NewActivity.this.FavoritosActivo = Boolean.valueOf(false);
                  NewActivity.this.web1.loadUrl("javascript:(function() { document.getElementById('left').style.visibility='hidden';})()");
                } else {
                  NewActivity.this.FavoritosActivo = Boolean.valueOf(true);
                  NewActivity.this.web1.loadUrl("javascript:(function() { document.getElementById('left').style.visibility=null;})()");
                }
              //  SQLiteDatabase bd = new AdminSQLiteOpenHelper(NewActivity.this.getApplicationContext(), "libros", null, 1).getWritableDatabase();
                Cursor fila = bd.rawQuery("SELECT HOJA FROM SEPARADORES WHERE LIBROID=" + NewActivity.this.libroid, null);
                ArrayList<String> Separadores = new ArrayList();
                while (fila.moveToNext()) {
                  Separadores.add(fila.getString(0));
                }
                NewActivity.this.web1.loadUrl("javascript:" + ("Visor.LstSeparadores(" + new JSONArray(Separadores).toString() + ")"));
              //  bd.close();
                fila.close();
              }

             /*   String js = "document.getElementById('btnListaSeperadores').click()";
                web1.loadUrl("javascript:" + js);*/
            }
        });
        FabIndice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)  {
                //if(SubRayar==true || RayarActivo==true) return;
              if(RayarActivo==true)FabCloseRayar.callOnClick();
              if(FotoActivo==true)FabCloseFoto.callOnClick();
              if(SubRayar==true) FabCloseSubrayar.callOnClick();


              //NewActivity.this.web1.loadUrl("javascript:(function() { document.getElementById('btnIndice').click(); })()");
              //ResetZoom();
              CloseFabMenu();
              List<String> mAnimals = new ArrayList<String>();

              try{


                  JSONArray jsonArray = new JSONArray(Capitulos);


                  for (int i = 0; i < jsonArray.length(); i++) {
                      mAnimals.add(jsonArray.getString(i));
                  }

              }
              catch (JSONException e)
              {

              }


                //Create sequence of items
                final CharSequence[] Animals = mAnimals.toArray(new String[mAnimals.size()]);
                // AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(NewActivity.this.getApplicationContext());
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(NewActivity.this);
                dialogBuilder.setTitle("Índice");
                dialogBuilder.setItems(Animals, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        String selectedText = Animals[item].toString();  //Selected item in listview
                        String js = "Visor.handleIndiceNative('" + selectedText + "')";
                        web1.loadUrl("javascript:" + js);

                        //this.web1.loadUrl("javascript:" + ("alert('hola'"));
                    }
                });


                //Create alert dialog object via builder

                AlertDialog alertDialogObject = dialogBuilder.create();

                //Show the dialog
                alertDialogObject.show();


            }
        });
      txtNumeroPagina.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if(SubRayar==true || RayarActivo==true) return;

          //ResetZoom();
          CloseFabMenu();
          List<String> indice = new ArrayList<String>();

          for(int i=1;i <= TotalPaginas;i++){
            indice.add(""+i);
          }

          //Create sequence of items
          final CharSequence[] Indices = indice.toArray(new String[indice.size()]);

          AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(NewActivity.this);
          dialogBuilder.setTitle("Pagina");
          dialogBuilder.setItems(Indices, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
              String selectedText = Indices[item].toString();  //Selected item in listview
              selectedText=selectedText.replace("●","");
              web1.loadUrl("javascript:" + ("IDRViewer.goToPage(" + selectedText + ")"));
                HojaActual=selectedText;
                txtNumeroPagina.setText(selectedText);
            }
          });
          //Create alert dialog object via builder
          AlertDialog alertDialogObject = dialogBuilder.create();
          //Show the dialog
          alertDialogObject.show();
          //alertDialogObject.getWindow().setLayout(600, 600);
                /*String js = "document.getElementById('btnIndice').click()";
                web1.loadUrl("javascript:" + js);*/
        }
      });
        FabNotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // if(SubRayar==true || RayarActivo==true) return;
              if(RayarActivo==true)FabCloseRayar.callOnClick();
              if(FotoActivo==true)FabCloseFoto.callOnClick();
              if(SubRayar==true) FabCloseSubrayar.callOnClick();

              //    final SQLiteDatabase bd = new AdminSQLiteOpenHelper(NewActivity.this.getApplicationContext(), "libros", null, 1).getWritableDatabase();
                  NewActivity.this.CloseFabMenu();
                  Cursor fila = bd.rawQuery("SELECT * FROM NOTAS WHERE HOJA='" + NewActivity.this.HojaActual + "' and LIBROID=" + NewActivity.this.libroid, null);
                  NewActivity.this.e2.setText("");

                NewActivity.this.ThisDialog.show();

              if (fila.moveToFirst()) {
                NewActivity.this.ThisDialog.show();
               // Cursor fila1 = bd.rawQuery("SELECT DATA FROM NOTAS WHERE HOJA='" + NewActivity.this.HojaActual + "' and LIBROID=" + NewActivity.this.libroid, null);
                NewActivity.this.e2.setText(fila.getString(3));
                NewActivity.this.e2.setSelection(NewActivity.this.e2.getText().length());
                NewActivity.this.guardar.setOnClickListener(new View.OnClickListener() {
                  public void onClick(View v) {
                    if (!TextUtils.isEmpty(NewActivity.this.e2.getText().toString())) {
                      bd.update("NOTAS", NewActivity.this.PutBDNota(), "HOJA='" + NewActivity.this.HojaActual + "' AND LIBROID=" + NewActivity.this.libroid, null);
                     // String js = "Visor.dibujarNota(" + NewActivity.this.HojaActual + ")";
                  //    bd.close();

                    //  NewActivity.this.web1.loadUrl("javascript:" + js);
                      NewActivity.this.ThisDialog.hide();

                    }
                  }
                });
                NewActivity.this.borrar.setOnClickListener(new View.OnClickListener() {
                  public void onClick(View v) {
                    try {
                      bd.delete("NOTAS", "HOJA='" + NewActivity.this.HojaActual + "' AND LIBROID=" + NewActivity.this.libroid, null);
                      String js = "Visor.borrarNota(" + NewActivity.this.HojaActual + ")";
                    //  bd.close();
                      NewActivity.this.web1.loadUrl("javascript:(function() { document.getElementById('BtnEliminarNota').click();})()");
                      NewActivity.this.ThisDialog.hide();
                      NewActivity.this.e2.setText("");
                    } catch (Exception e) {
                      e.printStackTrace();
                    }
                  }
                });
                return;
              }
              fila.close();

                NewActivity.this.guardar.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                  if (!TextUtils.isEmpty(NewActivity.this.e2.getText().toString())) {
                    bd.insert("NOTAS", null, NewActivity.this.PutBDNota());
                    String js = "Visor.dibujarNota(" + NewActivity.this.HojaActual + ")";
                   // bd.close();
                    NewActivity.this.web1.loadUrl("javascript:" + js);
                    NewActivity.this.ThisDialog.hide();
                  }
                }
              });
              NewActivity.this.borrar.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                  try {
                    NewActivity.this.ThisDialog.hide();
                    NewActivity.this.e2.setText("");
                  } catch (Exception e) {
                    e.printStackTrace();
                  }
                }
              });
            //  return;


             /*   ResetZoom();
                CloseFabMenu();
                String js = "document.getElementById('btnNota').click()";
                web1.loadUrl("javascript:" + js);*/
            }
        });
        FabRayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(SubRayar==true) FabCloseSubrayar.callOnClick();
                if(FotoActivo==true)FabCloseFoto.callOnClick();

                if(RayarActivo==false) {
                    int inicio;
                    int fin;
                    RayarActivo=true;
                    CloseFabMenu();

                    simpleSeekBar.setProgress(10);

                    FabRayarColor.setBackgroundResource(getResources().getIdentifier("fabbuttonblack", "drawable", getPackageName()));
        //            web1.loadUrl("javascript:" + "Visor.handleColorRayado('black')");

                    FabUndo.setVisibility(View.VISIBLE);
                    FabLimpiar.setVisibility(View.VISIBLE);
                    FabCloseRayar.setVisibility(View.VISIBLE);
                    FabRayarColor.setVisibility(View.VISIBLE);
                    FabTamano.setVisibility(View.VISIBLE);
                    FabRayar.setImageResource(getResources().getIdentifier("pencil_box", "drawable", getPackageName()));
                    web1.loadUrl("javascript:(function() { document.getElementById('BtnColor').style['background-color']='black';document.getElementById('idrviewer').style['touch-action']  = 'none'})()");

                  int PaginaActual = Integer.parseInt(HojaActual);
                  if (PaginaActual < 1 || PaginaActual >= TotalPaginas) {
                    inicio = PaginaActual - 1;
                    fin = PaginaActual;
                  } else {
                    inicio = PaginaActual == 1 ? PaginaActual : PaginaActual - 1;
                    fin = PaginaActual == 1 ? PaginaActual + 1 : PaginaActual + 1;
                  }

                 // SQLiteDatabase bd = new AdminSQLiteOpenHelper(getApplicationContext(), "libros", null, 1).getWritableDatabase();
                 for (int i = inicio; i <= fin; i++) {
                    Cursor fila = bd.rawQuery("SELECT DATA FROM RAYADO WHERE HOJA='" + i + "' and LIBROID=" + libroid, null);
                    if (fila.moveToFirst()) {
                      web1.loadUrl("javascript:" + ("Visor.ActivarRayado('" + fila.getString(0) + "'," + i + ")"));

                    } else {
                      web1.loadUrl("javascript:" + ("Visor.dibujarRayado(" + i + ",null)"));
                    }
                    fila.close();
                  }
                    web1.loadUrl("javascript:" + "(Visor.handleTamanoLienzo('1.0'))");
                 // bd.close();
                }
                else {
                    RayarActivo=false;
                    FabUndo.setVisibility(View.GONE);
                    FabLimpiar.setVisibility(View.GONE);
                    FabCloseRayar.setVisibility(View.GONE);
                    FabRayarColor.setVisibility(View.GONE);
                    FabTamano.setVisibility(View.GONE);
                    simpleSeekBar.setVisibility(View.GONE);
                    FabRayar.setImageResource(getResources().getIdentifier("pencil_box_outline", "drawable", getPackageName()));
                    web1.loadUrl("javascript:Visor.DesactivarRayado();");
                }
                //String js = "document.getElementById('btnMarcar').click()";
                //web1.loadUrl("javascript:" + js);
            }
        });
        /***************Panel Rayar**********************/
        FabRayarColor.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            if(PanelRayado.getVisibility() == View.GONE)
              PanelRayado.setVisibility(View.VISIBLE);
            else
              PanelRayado.setVisibility(View.GONE);
          }
        });

        simpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;


            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
                System.out.println(progressChangedValue);
                division=(double)progressChangedValue/10;
                if(progressChangedValue<=5){
                    FabTamano.setText(""+0.5);
                    division=0.5;
                }
                else{
                    FabTamano.setText(""+division);
                }

            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                web1.loadUrl("javascript:" + "Visor.handleTamanoLienzo('"+ division +"')");
                FabRayarColor.setVisibility(View.VISIBLE);
                FabUndo.setVisibility(View.VISIBLE);
                FabCloseRayar.setVisibility(View.VISIBLE);
                FabLimpiar.setVisibility(View.VISIBLE);
                simpleSeekBar.setVisibility(View.GONE);
                progresvisible=1;
            }
        });

        FabTamano.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                   if(progresvisible==1){
                       FabRayarColor.setVisibility(View.GONE);
                       FabUndo.setVisibility(View.GONE);
                       FabCloseRayar.setVisibility(View.GONE);
                       FabLimpiar.setVisibility(View.GONE);
                       PanelRayado.setVisibility(View.GONE);
                       simpleSeekBar.setVisibility(View.VISIBLE);
                       progresvisible=0;
                   }
                   else if(progresvisible==0){
                       FabRayarColor.setVisibility(View.VISIBLE);
                       FabUndo.setVisibility(View.VISIBLE);
                       FabCloseRayar.setVisibility(View.VISIBLE);
                       FabLimpiar.setVisibility(View.VISIBLE);
                       simpleSeekBar.setVisibility(View.GONE);
                       progresvisible=1;

                }
            }
        });
        FabRayarNegro.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            FabRayarColor.setBackgroundResource(getResources().getIdentifier("fabbuttonblack", "drawable", getPackageName()));
            web1.loadUrl("javascript:" + "Visor.handleColorRayado('black')");
            web1.loadUrl("javascript:" + "Visor.handleTamanoLienzo('"+ FabTamano.getText().toString() +"')");
            PanelRayado.setVisibility(View.GONE);
          }
        });
        FabRayarRojo.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            FabRayarColor.setBackgroundResource(getResources().getIdentifier("fabbuttonred", "drawable", getPackageName()));
            web1.loadUrl("javascript:" + "Visor.handleColorRayado('red')");
            web1.loadUrl("javascript:" + "Visor.handleTamanoLienzo('"+ FabTamano.getText().toString() +"')");
            PanelRayado.setVisibility(View.GONE);
          }
        });
        FabRayarAmarillo.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            FabRayarColor.setBackgroundResource(getResources().getIdentifier("fabbuttonyellow", "drawable", getPackageName()));
            web1.loadUrl("javascript:" + "Visor.handleColorRayado('yellow')");
            web1.loadUrl("javascript:" + "Visor.handleTamanoLienzo('"+ FabTamano.getText().toString() +"')");
            PanelRayado.setVisibility(View.GONE);
          }
        });
        FabRayarVerde.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            FabRayarColor.setBackgroundResource(getResources().getIdentifier("fabbuttongreen", "drawable", getPackageName()));
            web1.loadUrl("javascript:" + "Visor.handleColorRayado('green')");
            web1.loadUrl("javascript:" + "Visor.handleTamanoLienzo('"+ FabTamano.getText().toString() +"')");
            PanelRayado.setVisibility(View.GONE);
          }
        });
        FabRayarRosa.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            FabRayarColor.setBackgroundResource(getResources().getIdentifier("fabbuttonpink2", "drawable", getPackageName()));
            web1.loadUrl("javascript:" + "Visor.handleColorRayado('purple')");
            web1.loadUrl("javascript:" + "Visor.handleTamanoLienzo('"+ FabTamano.getText().toString() +"')");
            PanelRayado.setVisibility(View.GONE);
          }
        });
        FabRayarAzul.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            FabRayarColor.setBackgroundResource(getResources().getIdentifier("fabbuttonblue2", "drawable", getPackageName()));
            web1.loadUrl("javascript:" + "Visor.handleColorRayado('blue')");
            web1.loadUrl("javascript:" + "Visor.handleTamanoLienzo('"+ FabTamano.getText().toString() +"')");
            PanelRayado.setVisibility(View.GONE);
          }
        });
        FabRayarRosaLigero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FabRayarColor.setBackgroundResource(getResources().getIdentifier("fabbuttonpinkligth", "drawable", getPackageName()));
                web1.loadUrl("javascript:" + "Visor.handleColorRayado('magenta')");
                web1.loadUrl("javascript:" + "Visor.handleTamanoLienzo('"+ FabTamano.getText().toString() +"')");
                PanelRayado.setVisibility(View.GONE);
            }
        });
        FabRayarGrey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FabRayarColor.setBackgroundResource(getResources().getIdentifier("fabbuttongrey", "drawable", getPackageName()));
                web1.loadUrl("javascript:" + "Visor.handleColorRayado('brown')");
                web1.loadUrl("javascript:" + "Visor.handleTamanoLienzo('"+ FabTamano.getText().toString() +"')");
                PanelRayado.setVisibility(View.GONE);
            }
        });
        FabRayarMarron.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FabRayarColor.setBackgroundResource(getResources().getIdentifier("fabbuttonmarron", "drawable", getPackageName()));
                web1.loadUrl("javascript:" + "Visor.handleColorRayado('orange')");
                web1.loadUrl("javascript:" + "Visor.handleTamanoLienzo('"+ FabTamano.getText().toString() +"')");
                PanelRayado.setVisibility(View.GONE);
            }
        });
        /***********************************************/
        FabSubrayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(RayarActivo==true)FabCloseRayar.callOnClick();
                if(FotoActivo==true)FabCloseFoto.callOnClick();

                if(SubRayar==false) {
                    int inicio;
                    int fin;
                    SubRayar=true;
                    CloseFabMenu();
                    FabBorrar.setVisibility(View.VISIBLE);
                    FabCloseSubrayar.setVisibility(View.VISIBLE);
                    FabColorSubrayado.setVisibility(View.VISIBLE);
                    //FabSubrayar.setImageResource(getResources().getIdentifier("format_strikethrough_variant", "drawable", getPackageName()));
                    FabColorSubrayado.setBackgroundResource(getResources().getIdentifier("fabbuttonyellow", "drawable", getPackageName()));
                    web1.loadUrl("javascript:" + "Visor.handleColorSubRayado('rgba(255, 255, 0, 0.25)')");
                    web1.loadUrl("javascript:(function() { document.ontouchmove = function(e){ e.preventDefault(); }})()");
                  int PaginaActual = Integer.parseInt(HojaActual);
                  if (PaginaActual < 1 || PaginaActual >= TotalPaginas) {
                      inicio = PaginaActual - 1;
                      fin = PaginaActual;
                    } else {
                      inicio = PaginaActual == 1 ? PaginaActual : PaginaActual - 1;
                      fin = PaginaActual == 1 ? PaginaActual + 1 : PaginaActual + 1;
                    }
                    for (int i = inicio; i <= fin; i++) {
                       web1.loadUrl("javascript:" + ("Visor.ActivarSubrayado(" + i + ")"));
                    }
                }
                else{
                    SubRayar=false;
                    FabBorrar.setVisibility(View.GONE);
                    FabCloseSubrayar.setVisibility(View.GONE);
                    FabColorSubrayado.setVisibility(View.GONE);
                    FabSubrayar.setImageResource(getResources().getIdentifier("format_underline", "drawable", getPackageName()));
                    web1.loadUrl("javascript:" + "Visor.DesactivarSubrayado()");
                    web1.loadUrl("javascript:(function() { document.ontouchmove = function(e){  }})()");
                }

                //web1.loadUrl("javascript:" + "document.getElementById('btnSubrayar').click()");
                /*String js = "document.getElementById('btnSubrayar').click()";
                web1.loadUrl("javascript:" + js);*/
            }
        });
      /********************Panel color subrayado**********************/
      FabColorSubrayado.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(PanelSubrayado.getVisibility() == View.GONE)
                PanelSubrayado.setVisibility(View.VISIBLE);
            else
               PanelSubrayado.setVisibility(View.GONE);
        }
      });
      FabSubrayarAmarillo.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          FabColorSubrayado.setBackgroundResource(getResources().getIdentifier("fabbuttonyellow", "drawable", getPackageName()));
          web1.loadUrl("javascript:" + "Visor.handleColorSubRayado('rgba(255, 255, 0, 0.25)')");
          PanelSubrayado.setVisibility(View.GONE);
        }
      });
      FabSubrayarAzul.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          FabColorSubrayado.setBackgroundResource(getResources().getIdentifier("fabbuttonblue", "drawable", getPackageName()));
          web1.loadUrl("javascript:" + "Visor.handleColorSubRayado('rgba(39, 255, 240, 0.25)')");
          PanelSubrayado.setVisibility(View.GONE);
        }
      });
      FabSubrayarRosa.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          FabColorSubrayado.setBackgroundResource(getResources().getIdentifier("fabbuttonpink", "drawable", getPackageName()));
          web1.loadUrl("javascript:" + "Visor.handleColorSubRayado('rgba(255, 39, 123, 0.25)')");
          PanelSubrayado.setVisibility(View.GONE);
        }
      });
      /*************************************************************/
      FabFoto.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if(RayarActivo==true)FabCloseRayar.callOnClick();
          if(SubRayar==true) FabCloseSubrayar.callOnClick();


          if(FotoActivo==false) {
            FotoActivo=true;
            CloseFabMenu();
            FabTomarFoto.setVisibility(View.VISIBLE);
            FabGaleria.setVisibility(View.VISIBLE);
            FabCloseFoto.setVisibility(View.VISIBLE);
            FabRayar.setImageResource(getResources().getIdentifier("pencil_box", "drawable", getPackageName()));


          }
          else {
            FotoActivo=false;
            FabTomarFoto.setVisibility(View.GONE);
            FabGaleria.setVisibility(View.GONE);
            FabCloseFoto.setVisibility(View.GONE);
            FabRayar.setImageResource(getResources().getIdentifier("pencil_box_outline", "drawable", getPackageName()));
          }
                /*String js = "document.getElementById('btnMarcar').click()";
                web1.loadUrl("javascript:" + js);*/
        }
      });

        /***Herramientas Subrayar***/
        FabBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(BorrarActivo==false){
                    BorrarActivo=true;
                    FabBorrarActivo.setVisibility(View.VISIBLE);
                    FabBorrar.setVisibility(View.GONE);
                }
                String js = "document.getElementById('BtnBorrarSubrayado').click()";
                web1.loadUrl("javascript:" + js);
            }
        });
        FabBorrarActivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(BorrarActivo==true)
                {
                    BorrarActivo=false;
                    FabBorrarActivo.setVisibility(View.GONE);
                    FabBorrar.setVisibility(View.VISIBLE);
                }

                String js = "document.getElementById('BtnBorrarSubrayado').click()";
                web1.loadUrl("javascript:" + js);
            }
        });
        FabCloseSubrayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BorrarActivo=false;
                FabBorrarActivo.setVisibility(View.GONE);
                FabBorrar.setVisibility(View.VISIBLE);
                PanelSubrayado.setVisibility(View.GONE);
                FabSubrayar.callOnClick();
            }
        });
        /***************************/
        /***Herramientas Rayar******/
        FabUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String js = "document.getElementById('BtnUndo').click()";
                web1.loadUrl("javascript:" + js);
            }
        });
        FabLimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String js = "document.getElementById('BtnClear').click()";
                web1.loadUrl("javascript:" + js);
            }
        });
        FabCloseRayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FabRayar.callOnClick();
                PanelRayado.setVisibility(View.GONE);
                FabCloseRayar.setVisibility(View.GONE);
            }
        });
      /***************************/
      /***Herramientas Foto******/
      FabTomarFoto.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

          checarpermisocamara(null,null,1);

        //  NewActivity.this.web1.loadUrl("javascript:" + js);
        //  NewActivity.this.ThisDialog.hide();
        }
      });
      FabGaleria.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {


          Galeria();


        }
      });
      FabCloseFoto.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          FabFoto.callOnClick();
         /* FabTomarFoto.setVisibility(View.GONE);
          FabGaleria.setVisibility(View.GONE);
          FabCloseFoto.setVisibility(View.GONE);*/

        }
      });
        FabEjercicios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          /*  CloseFabMenu();
            txtNumeroPagina.setVisibility(View.GONE);
            FabMain.setVisibility(View.GONE);
            FabBookmark.setVisibility(View.GONE);
            FabFavoritos.setVisibility(View.GONE);
            FabIndice.setVisibility(View.GONE);
            FabNotas.setVisibility(View.GONE);
            FabFoto.setVisibility(View.GONE);
            FabRayar.setVisibility(View.GONE);
            FabSubrayar.setVisibility(View.GONE);
            FabEjercicios.setVisibility(View.GONE);
            FabBorrarActivo.setVisibility(View.GONE);
            FabBorrar.setVisibility(View.GONE);
            PanelSubrayado.setVisibility(View.GONE);
            FabTomarFoto.setVisibility(View.GONE);
            FabGaleria.setVisibility(View.GONE);
            FabCloseFoto.setVisibility(View.GONE);
            FabBorrar.setVisibility(View.GONE);
            FabCloseSubrayar.setVisibility(View.GONE);
            FabColorSubrayado.setVisibility(View.GONE);
            FabNumeroPagina.setVisibility(View.GONE);
            FabCalificarOpen.setVisibility(View.GONE);
            FabCalificar.setVisibility(View.GONE);
            FabEjercicios.setVisibility(View.GONE);

            web1.loadUrl("javascript:Visor.VerEjercicios();");*/


            }
        });
        /***************************/
        // Create a `GestureDetector` that does something special on double-tap.
        final GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent event) {
                // TODO: Insert code to run on double-tap here.
                ResetZoom();
                // Consume the double-tap.
                return true;
            }
            @Override
            public boolean onSingleTapConfirmed(MotionEvent event){
                if(BorrarActivo==true)
                        return true;
                if(TapActivo==false) {
                    TapActivo=true;
                    FabMain.setVisibility(View.GONE);
                    FabBookmark.setVisibility(View.GONE);
                    FabFavoritos.setVisibility(View.GONE);
                    FabIndice.setVisibility(View.GONE);
                    FabNotas.setVisibility(View.GONE);
                    FabFoto.setVisibility(View.GONE);
                    FabRayar.setVisibility(View.GONE);
                    FabSubrayar.setVisibility(View.GONE);
                }
                else{
                    TapActivo=false;
                    FabMain.setVisibility(View.VISIBLE);
                    FabBookmark.setVisibility(View.VISIBLE);
                    FabFavoritos.setVisibility(View.VISIBLE);
                    FabIndice.setVisibility(View.VISIBLE);
                    FabNotas.setVisibility(View.VISIBLE);
                    FabFoto.setVisibility(View.VISIBLE);
                    FabRayar.setVisibility(View.VISIBLE);
                    FabSubrayar.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });

        /*web1.getSettings().setJavaScriptEnabled(true);
        web1.getSettings().setBuiltInZoomControls(true);
        web1.getSettings().setDisplayZoomControls(false);
        web1.getSettings().setAllowFileAccessFromFileURLs(true);
        web1.getSettings().setAllowUniversalAccessFromFileURLs(true);
        web1.getSettings().setAllowFileAccess(true);
        web1.getSettings().setDomStorageEnabled(true);
		//Temporal //WebView
        web1.setWebContentsDebuggingEnabled(true);*/

		// Set the `WebView` to pass touch events through `gestureDetector`.
        /*web1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });*/
        /*web1.setWebViewClient(new SystemWebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(SystemWebView view, String url) {
                return false;
            }
        });*/

        //web1.loadUrl("file:///storage/emulated/0/Android/data/io.ionic.starter/files/Libro2/index.html?page=1");
    //    super.init();
        // Load your application
        Bundle bundle = getIntent().getExtras();
        libroid = Integer.parseInt(bundle.getString("libroid"));
        launchUrl = bundle.getString("path");
        loadUrl(launchUrl + "/indexAndroid.html?page=1");
        web1.setWebViewClient(new SystemWebViewClient((SystemWebViewEngine) this.appView.getEngine()) {
          public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            NewActivity newActivity = NewActivity.this;
            cargando++;
            if (cargando == 2) {
              System.out.println("cargando------------------------!!!!!!!!!!!!!!!!!!!!");
             dibujarSeparadores();
             dibujarRayado();
             dibujarSubrayado();
             dibujarNotas();
             dibujarFotos();
             cargarCapitulos();
            }
           }
         });

    }
    public void cargarCapitulos(){
        String js = "Visor.handleIndice()";
        web1.loadUrl("javascript:" + js);
    }
    public void dibujarSeparadores() {
   //   SQLiteDatabase bd = new AdminSQLiteOpenHelper(getApplicationContext(), "libros", null, 1).getWritableDatabase();
      Cursor fila = bd.rawQuery("SELECT HOJA FROM SEPARADORES WHERE LIBROID=" + this.libroid, null);

      while (fila.moveToNext()) {
        web1.loadUrl("javascript:" + ("Visor.dibujarSeparador(" + fila.getString(0) + ")"));
      }
      fila.close();
   //   bd.close();
    }
  public void dibujarNotas() {
   // SQLiteDatabase bd = new AdminSQLiteOpenHelper(getApplicationContext(), "libros", null, 1).getWritableDatabase();
    Cursor fila = bd.rawQuery("SELECT HOJA FROM NOTAS WHERE LIBROID=" + this.libroid, null);
    while (fila.moveToNext()) {
      this.web1.loadUrl("javascript:" + ("Visor.dibujarNota(" + fila.getString(0) + ")"));
    }
  //  bd.close();
  }
  public void dibujarFotos() {
   // SQLiteDatabase bd = new AdminSQLiteOpenHelper(getApplicationContext(), "libros", null, 1).getWritableDatabase();
    Cursor fila = bd.rawQuery("SELECT HOJA FROM FOTOS WHERE LIBROID=" + this.libroid, null);
    while (fila.moveToNext()) {
      this.web1.loadUrl("javascript:" + ("Visor.dibujarFoto(" + fila.getString(0) + ")"));
    }
    fila.close();
   // bd.close();
  }
    public void dibujarRayado() {
     // SQLiteDatabase bd = new AdminSQLiteOpenHelper(getApplicationContext(), "libros", null, 1).getWritableDatabase();
      Cursor fila = bd.rawQuery("SELECT DATA,HOJA FROM RAYADO WHERE LIBROID=" + this.libroid, null);
      while (fila.moveToNext()) {
        String data = fila.getString(0);
        String hoja = fila.getString(1);
        web1.loadUrl("javascript:" + ("Visor.dibujarRayado(" + hoja + ",'" + data + "')"));
        web1.loadUrl("javascript:(function() { document.getElementById('signatureCanvas" + hoja + "').style['touch-action']=null;})()");
      }
      fila.close();
   //   bd.close();

    }
 /* public void dibujarEscribir(String ejercicio) {
   // SQLiteDatabase bd = new AdminSQLiteOpenHelper(getApplicationContext(), "libros", null, 1).getWritableDatabase();
    Cursor fila = bd.rawQuery("SELECT DATA,ELEMENTO,ESTADO FROM ESCRIBIR WHERE LIBROID=" + libroid   , null);
    while (fila.moveToNext()) {
      String data = fila.getString(0);
      String Elemento = fila.getString(1);
      String Estado = fila.getString(2);
      if (!data.equals("[]")) {
       // web1.loadUrl("javascript:document.getElementById('"+ Elemento+ "').value=("+ "'"+ data + "')");
          web1.post(new Runnable() {
              @Override
              public void run() {
                  web1.loadUrl("javascript: document.getElementById('e1_txt1').value=('Xxxxxxx');");
              }
          });

       System.out.println("javascript: document.getElementById('"+ Elemento+ "').value=("+ "'"+ data + "');");

      }
    }
    fila.close();
  //  bd.close();
  }*/
    public void dibujarSubrayado() {
        // SQLiteDatabase bd = new AdminSQLiteOpenHelper(getApplicationContext(), "libros", null, 1).getWritableDatabase();
        Cursor fila = bd.rawQuery("SELECT DATA,HOJA FROM SUBRAYADO WHERE LIBROID=" + libroid, null);
        while (fila.moveToNext()) {
            String data = fila.getString(0);
            String hoja = fila.getString(1);
            if (!data.equals("[]")) {
                this.web1.loadUrl("javascript:" + ("Visor.dibujarMarcatexto(" + hoja + ",'" + data + "')"));
            }
        }
        fila.close();
        //  bd.close();
    }
    private void ResetZoom(){
        web1.getSettings().setLoadWithOverviewMode(false);
        web1.getSettings().setLoadWithOverviewMode(true);
        web1.setInitialScale(0);
    }
  public ContentValues PutBDNota() {
    ContentValues registro = new ContentValues();
    registro.put("LIBROID", Integer.valueOf(this.libroid));
    registro.put("HOJA", this.HojaActual);
    registro.put("DATA", this.e2.getText().toString());
    return registro;
  }
  public ContentValues PutBDFoto() {
    ContentValues registro = new ContentValues();
    registro.put("LIBROID", Integer.valueOf(this.libroid));
    registro.put("HOJA", this.HojaActual);
    registro.put("DATA", getCurrentTimeStamp()+".png");
    return registro;
  }


  /****************************/
  /***METODOS PARA TOMAR FOTOGRAFIA***/

  private void checarpermisocamara(@Nullable String id, @Nullable String pid, int tipo){
      tipofoto=tipo;
      imagenid=id;
      paginaid=pid;
    int permiso = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
    if(permiso != PackageManager.PERMISSION_GRANTED){
      Log.i("Mensaje","Nose tiene permiso para la camara!." );
       ActivityCompat.requestPermissions(NewActivity.this, new String[]{Manifest.permission.CAMERA}, 225); {
            Log.i("Mensaje","Nose tiene permiso para la camara!." );
      };

    }else{
        tomarFoto(id,pid,tipo);
      Log.i("Mensaje","Tienes permiso para la camara!." );
    }


  }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
          if(requestCode==225 && grantResults[0]==0 )
            tomarFoto(imagenid,paginaid,tipofoto);


    }


  public void tomarFoto(@Nullable String id, @Nullable String pid, int tipo) {

//1=imagen tomada fuera  del libro 0=imagen tomada dentro del libro javascript

    File mydir;
    File foto;
    nombrearchivo=getCurrentTimeStamp();
      if(tipo==1){
          mydir = new File(getExternalFilesDir(null),"fotolibro"+libroid+"/"+HojaActual);
          if (!mydir.exists()) mydir.mkdirs();
          foto = new File(getExternalFilesDir(null), "fotolibro"+libroid+"/"+HojaActual+"/"+nombrearchivo+".png");
      }
      else {
          mydir = new File(getExternalFilesDir(null),"fotolibro"+libroid+"/"+paginaid);
          if (!mydir.exists()) mydir.mkdirs();
          foto = new File(getExternalFilesDir(null), "fotolibro"+libroid+"/"+paginaid+"/"+nombrearchivo+".png");
      }



      try {
          Intent abrircamara = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
          abrircamara.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(NewActivity.this, BuildConfig.APPLICATION_ID + ".fileOpener2.provider", foto));

          startActivityForResult(abrircamara,CAMERA_REQUEST);
      } catch (Exception e) {
          Log.i("Error",e.toString() );
      }
  //  File foto = new File(getExternalFilesDir(null), "fotolibro"+libroid+"/"+HojaActual+"/"+getCurrentTimeStamp()+".jpg");



    }

@Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
        //1=imagen tomada fuera  del libro 0=imagen tomada dentro del libro javascript



        if(tipofoto==1){

            File foto = new File(getExternalFilesDir(null), "fotolibro"+libroid+"/"+NewActivity.this.HojaActual+"/"+nombrearchivo+".png");
            File newfile = new File(NewActivity.this.getFilesDir() + "/books2020/"+"Libro"+libroid+"/"+NewActivity.this.HojaActual+"/img");

            copyFile(foto,newfile,nombrearchivo);
            comprimirimage(newfile,nombrearchivo,data,HojaActual);
            //    final SQLiteDatabase bd = new AdminSQLiteOpenHelper(NewActivity.this.getApplicationContext(), "libros", null, 1).getWritableDatabase();
                Cursor fila = bd.rawQuery("SELECT * FROM FOTOS WHERE HOJA='" + NewActivity.this.HojaActual + "' and LIBROID=" + NewActivity.this.libroid, null);
                bd.insert("FOTOS", null, NewActivity.this.PutBDFoto());
              String js = "Visor.dibujarFoto(" + NewActivity.this.HojaActual + ")";
              NewActivity.this.web1.loadUrl("javascript:" + js);
              //String js = "Visor.dibujarNota(" + NewActivity.this.HojaActual + ")";
             //  bd.close();
              fila.close();
         }
         else{
            File foto = new File(getExternalFilesDir(null), "fotolibro"+libroid+"/"+paginaid+"/"+nombrearchivo+".png");
            File newfile = new File(NewActivity.this.getFilesDir() + "/books2020/"+"Libro"+libroid+"/"+paginaid);

            copyFile(foto,newfile,imagenid);
            comprimirimage(newfile,imagenid,data,paginaid);

            String js = "Visor.mostrarFoto("+"'"+  imagenid + "'" + "," +"'" + paginaid + "'"  + ")";
            NewActivity.this.web1.loadUrl("javascript:" + js);
        }
    }
}



    public void borrarFotoAdentroLibro(String id,String pid) {

        tipofoto=1;
        imagenid=id;
        paginaid=pid;

        File mydir = new File(getFilesDir(),"books2020/Libro"+libroid+"/"+paginaid+"/"+id+".png");

        mydir.delete();
        String js = "Visor.LimpiarFoto("+"'"+id+"'"+ ")";
        NewActivity.this.web1.loadUrl("javascript:" + js);


    }


  public  void Galeria(){
    getFromSdcard();
    borrargaleria.clear();
    ThisDialogGrid=new Dialog(NewActivity.this);
    ThisDialogGrid.requestWindowFeature(1);
   // ThisDialogGrid.setContentView(R.layout.modalgrid);
    //ThisDialogGrid.setContentView(findViewById(getResources().getIdentifier("modalgrid", "id", getApplication().getPackageName())));
    ThisDialogGrid.setContentView(getResources().getIdentifier("modalgrid", "layout", getPackageName()));
    ThisDialogGrid.show();
    //Botones galeria
    this.btnBorrarImagen = (ImageButton) this.ThisDialogGrid.findViewById(getResources().getIdentifier("btnBorrarImagen", "id",getPackageName()));
    this.btnBorrarImagen.setVisibility(View.GONE);

//    androidGridView = (GridView) ThisDialogGrid.findViewById(R.layout.modalgrid);
    androidGridView = (GridView) ThisDialogGrid.findViewById(getResources().getIdentifier("gridview_android_example", "id", getPackageName()));
    androidGridView.setAdapter(new ImageAdapterGridView(this));
    androidGridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
    androidGridView.setMultiChoiceModeListener(new MultiChoiceModeListener());

    androidGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      public void onItemClick(AdapterView<?> parent,
                              View v, int position, long id) {
        String rutaimagen= new ImageAdapterGridView(NewActivity.this).getItem(position).toString();
       /* Intent intent=new Intent(Intent.ACTION_VIEW);
        Uri data=Build.VERSION.SDK_INT >= 24 ? Uri.parse(rutaimagen) : Uri.fromFile(new File(rutaimagen));
        intent.setDataAndType(data,"image/*");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);*/

         /* Uri data=Build.VERSION.SDK_INT >= 24 ? Uri.parse(rutaimagen) : Uri.fromFile(new File(rutaimagen));
          Intent galleryIntent = new Intent(Intent.ACTION_VIEW, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
          galleryIntent.setDataAndType(data, "image/*");
          galleryIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_GRANT_READ_URI_PERMISSION);
          startActivity(galleryIntent);*/
          Uri data=Build.VERSION.SDK_INT >= 24 ? Uri.parse(rutaimagen) : Uri.fromFile(new File(rutaimagen));
          Intent intent = new Intent(NewActivity.this,
                  VisorImagen.class);
          intent.putExtra("img", rutaimagen);
         // intent.setDataAndType(data,"image/*");
          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| FLAG_GRANT_READ_URI_PERMISSION);
          startActivity(intent);
      }
    });


    btnBorrarImagen.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        SQLiteDatabase bd = new AdminSQLiteOpenHelper(getApplicationContext(), "libros", null, 1).getWritableDatabase();
        for (int i = 0; i < borrargaleria.size(); i++){
              File filenew= new File(borrargaleria.get(i));
              filenew.delete();
        }
        File file= new File(getFilesDir(),"books2020/Libro"+libroid+"/"+HojaActual+"/img");
       if(file.listFiles().length<1){
         bd.delete("FOTOS", "HOJA='" + HojaActual + "'", null);
         String js = "Visor.EliminarEtiquetaFoto(" + HojaActual + ")";
         web1.loadUrl("javascript:" + js);
       }
        ThisDialogGrid.onBackPressed();

        btnBorrarImagen.setVisibility(View.GONE);
      }
    });

  }

public void BotonesHerramientas(int espacio,int espacio1, int espacio3 , int espacio4){
    int sizeInDP = espacio+100;
    int sizeInDP1 = espacio1+100;
    int sizeInDP3 = espacio3+100;
    int sizeInDP4 = espacio4+100;
    int marginInDp = (int) TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, sizeInDP, getResources()
                    .getDisplayMetrics());
    int marginInDp1 = (int) TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, sizeInDP1, getResources()
                    .getDisplayMetrics());
    int marginInDp2 = (int) TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, sizeInDP3, getResources()
                    .getDisplayMetrics());
    int marginInDp3 = (int) TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, sizeInDP4, getResources()
                    .getDisplayMetrics());
    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) FabBorrar.getLayoutParams();
    params.bottomMargin = marginInDp;
    RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) FabColorSubrayado.getLayoutParams();
    params1.bottomMargin = marginInDp1;
    RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) FabGaleria.getLayoutParams();
    params2.bottomMargin = marginInDp;
    RelativeLayout.LayoutParams params3 = (RelativeLayout.LayoutParams) FabTomarFoto.getLayoutParams();
    params3.bottomMargin = marginInDp1;

    RelativeLayout.LayoutParams params4 = (RelativeLayout.LayoutParams) FabLimpiar.getLayoutParams();
    params4.bottomMargin = marginInDp;

    RelativeLayout.LayoutParams params5 = (RelativeLayout.LayoutParams) FabUndo.getLayoutParams();
    params5.bottomMargin = marginInDp1;

    RelativeLayout.LayoutParams params6 = (RelativeLayout.LayoutParams)FabRayarColor.getLayoutParams();
    params6.bottomMargin = marginInDp2;

    RelativeLayout.LayoutParams params7 = (RelativeLayout.LayoutParams)FabTamano.getLayoutParams();
    params7.bottomMargin = marginInDp3;


}
  public class MultiChoiceModeListener implements
    GridView.MultiChoiceModeListener {
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
      mode.setTitle("Selección de imagenes");

      return true;
    }

    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
      return true;
    }

    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
      return true;


    }

    public void onDestroyActionMode(ActionMode mode) {
      btnBorrarImagen.setVisibility(View.GONE);
    }

    public void onItemCheckedStateChanged(ActionMode mode, int position,
                                          long id, boolean checked) {
  //    int selectCount = androidGridView.getCheckedItemCount();
      String rutaimagen= new ImageAdapterGridView(NewActivity.this).getItem(position).toString();
      ImageView img =(ImageView)androidGridView.getChildAt(position);
      if(checked==true){

        borrargaleria.add(rutaimagen);


        img.setAlpha(0.8f);
        btnBorrarImagen.setVisibility(View.VISIBLE);
      }
      else{
        borrargaleria.remove(rutaimagen);
        img.setAlpha(1.0f);
      }

    /*  switch (selectCount) {
        case 1:
          mode.setSubtitle("One item selected");
          break;
        default:
          mode.setSubtitle("" + selectCount + " items selected");
          break;
      }*/
    }

  }

  public void getFromSdcard()
  {

    File file= new File(getFilesDir(),"books2020/Libro"+libroid+"/"+HojaActual+"/img");

    if (file.isDirectory())
    {
      f.clear();
      listFile = file.listFiles();


      for (int i = 0; i < listFile.length; i++)
      {

        f.add(listFile[i].getAbsolutePath());

      }
      //urls.addAll(f);
    }
    else {f.clear();}
  }

  public class ImageAdapterGridView extends BaseAdapter {
    private Context mContext;



    public ImageAdapterGridView(Context c) {
      mContext = c;
    }

    public int getCount() {
      // return imageIDs.length;
      return f.size();
    }

    @Override public String getItem(int position){
      return f.get(position);
    }

    public long getItemId(int position) {
      return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
      ImageView mImageView;

      if (convertView == null) {

          mImageView = new ImageView(mContext);
        mImageView.setLayoutParams(new GridView.LayoutParams(200, 230));
        mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //  mImageView.setPadding(0, 5, 0, 5);
        Picasso.get()
          // .load(f.get(position))
          //.load(imageIDs[position])
          .load("file://"+f.get(position))
          //   .noFade().resize(200, 230)
          .fit()
          .memoryPolicy(MemoryPolicy.NO_CACHE).centerCrop()
          .into(mImageView);
      } else {
        mImageView = (ImageView) convertView;
      }





      return mImageView;
    }
     /*   @Override public String getItem(int position){
            return urls.get(position);
        }*/
  }

  public static String getCurrentTimeStamp() {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    Date now = new Date();
    String strDate = sdf.format(now);
    return strDate;
  }

    public void copyFile(File src, File dst,String nombre)  {
        InputStream in = null;
        OutputStream out = null;


        try {
            if (!dst.getAbsoluteFile().exists())
            {
                dst.mkdirs();
            }
            in = new FileInputStream(src);
            out = new FileOutputStream(dst+"/"+nombre+".png");

            IOUtils.copy(in, out);
        } catch (IOException ioe) {
          //  Log.e(LOGTAG, "IOException occurred.", ioe);
        } finally {
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(in);
        }
    }

    public void comprimirimage(File dst,String nombre,  Intent data1,String hoja)  {


        File f = new File(dst+"/"+ nombre+".png");

        try {
            File resizedImage = new Resizer(this)
                    .setTargetLength(600)
                    .setQuality(40)
                    .setOutputFormat("PNG")
                    .setOutputFilename(nombre)
                    .setOutputDirPath(dst.getAbsolutePath())
                    .setSourceImage(f)
                    .getResizedFile();

        } catch (IOException e) {
            e.printStackTrace();
        }




    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        int a=Build.VERSION.SDK_INT;
        if(a>19){
            this.finish();
            Intent startIntent= new Intent(this, MainActivity.class);
            startIntent.putExtra("eduardo", "eduardo");
            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(startIntent);
        }

    }
    private void ShowFabMenu() {
      isFabOpen = true;

      FabBookmark.setVisibility(View.VISIBLE);
      FabFavoritos.setVisibility(View.VISIBLE);
      FabIndice.setVisibility(View.VISIBLE);
      FabNotas.setVisibility(View.VISIBLE);
      FabFoto.setVisibility(View.VISIBLE);
      FabRayar.setVisibility(View.VISIBLE);
      FabSubrayar.setVisibility(View.VISIBLE);

      int inicio=0;
      if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
     //   Toast.makeText(this, "xLarge screen", Toast.LENGTH_LONG).show();
          int a=Build.VERSION.SDK_INT;
          float densidad=Math.round(getResources().getDisplayMetrics().density);
          if(densidad==1.5 || densidad==1.0){
              BotonesHerramientas(180,250, 320,390);
              inicio=-90;
          }
          else if(densidad==2.0){
              //ya quedo
              BotonesHerramientas(180,250, 320,390);
              inicio=-130;
          }
          else if(densidad==3.0){
              BotonesHerramientas(180,250,320,390);
              inicio=-200;

              //LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) FabBorrar.getLayoutParams();
              //params.bottomMargin=200f;
          }
          else if(densidad==4.0){
              BotonesHerramientas(180,250,320,390);
              inicio=-260;

              //LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) FabBorrar.getLayoutParams();
              //params.bottomMargin=200f;
          }
      }
      else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
     //   Toast.makeText(this, "Large screen", Toast.LENGTH_LONG).show();

          float densidad=Math.round(getResources().getDisplayMetrics().density);
          if(densidad==1.5 || densidad==1.0){
              BotonesHerramientas(180,250,320,390);
              inicio=-80;
          }
          else if(densidad==2.0){
              //ya quedo
              BotonesHerramientas(180,250,320,390);

              inicio=-120;
          }
          else if(densidad==3.0){
              BotonesHerramientas(180,250,320,390);
              inicio=-190;

              //LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) FabBorrar.getLayoutParams();
              //params.bottomMargin=200f;
          }
          else if(densidad==4.0){
              BotonesHerramientas(180,250,320,390);
              inicio=-250;

              //LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) FabBorrar.getLayoutParams();
              //params.bottomMargin=200f;
          }

      }
      else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {

        float densidad=Math.round(getResources().getDisplayMetrics().density);
        //Toast.makeText(this, "Normal sized screen"+densidad, Toast.LENGTH_LONG).show();
          if(densidad==1.5 || densidad==1.0){
              BotonesHerramientas(160,210,260,310);
              inicio=-70;
          }
        else if(densidad==2.0){
              //ya quedo
              BotonesHerramientas(170,230,290,350);
              inicio=-110;
        }
        else if(densidad==3.0){
              //ya quedo
              BotonesHerramientas(170,230,290,350);
              inicio=-180;

          //LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) FabBorrar.getLayoutParams();
          //params.bottomMargin=200f;
        }
          else if(densidad==4.0){
              //YA QUEDO
              BotonesHerramientas(170,230,290,350);
              inicio=-240;

              //LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) FabBorrar.getLayoutParams();
              //params.bottomMargin=200f;
          }

      }
      else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {
     //   Toast.makeText(this, "Small sized screen", Toast.LENGTH_LONG).show();
      }
      else {
    //    Toast.makeText(this, "Screen size is neither large, normal or small", Toast.LENGTH_LONG).show();
        inicio=-120;
      }
      //int inicio=-120;

        int a=Build.VERSION.SDK_INT;
      if(a>19){
          FabMain.animate().rotation(135f);
      }

      FabBookmark.animate()
        .translationY(inicio * 1)
        .rotation(0f);
      FabFavoritos.animate()
        .translationY(inicio * 2)
        .rotation(0f);
      FabIndice.animate()
        .translationY(inicio * 3)
        .rotation(0f);
      FabNotas.animate()
        .translationY(inicio * 4)
        .rotation(0f);
      FabRayar.animate()
        .translationY(inicio * 5)
        .rotation(0f);
      FabSubrayar.animate()
        .translationY(inicio * 6)
        .rotation(0f);
      FabFoto.animate()
        .translationY(inicio * 7)
        .rotation(0f);
      FabNumeroPagina.animate()
        .translationY(inicio * 7);
      txtNumeroPagina.animate()
        .translationY(inicio * 7);

    }
    private void CloseFabMenu() {
        isFabOpen = false;

        FabMain.animate().rotation(0f);
        FabBookmark.animate()
                .translationY(0f)
                .rotation(90f);
        FabFavoritos.animate()
                .translationY(0f)
                .rotation(90f);
        FabIndice.animate()
                .translationY(0f)
                .rotation(90f);
        FabNotas.animate()
                .translationY(0f)
                .rotation(90f);
        FabRayar.animate()
                .translationY(0f)
                .rotation(90f);
        FabSubrayar.animate()
                .translationY(0f)
                .rotation(90f);
      FabFoto.animate()
                .translationY(0f)
                .rotation(90f);
      FabNumeroPagina.animate()
        .translationY(5f);
      txtNumeroPagina.animate()
        .translationY(5f);

        /*FabBookmark.setVisibility(View.GONE);
        FabFavoritos.setVisibility(View.GONE);
        FabIndice.setVisibility(View.GONE);
        FabNotas.setVisibility(View.GONE);
        FabRayar.setVisibility(View.GONE);
        FabSubrayar.setVisibility(View.GONE);*/

    }

    public void esconder(){
        FabMain.setVisibility(View.VISIBLE);
        FabNumeroPagina.setVisibility(View.VISIBLE);
        FabEjercicios.setVisibility(View.GONE);
        txtNumeroPagina.setVisibility(View.VISIBLE);
        this.FabCalificar.setVisibility(View.VISIBLE);
        this.FabCalificarOpen.setVisibility(View.GONE);
    }
    public void ocultarbotoncandado(String accion){
        if(accion.equals("abrir")){
            this.FabCalificarOpen.setVisibility(View.VISIBLE);
            this.FabCalificar.setVisibility(View.GONE);

        }
        else if(accion.equals("cerrar")){
            this.FabCalificar.setVisibility(View.VISIBLE);
            this.FabCalificarOpen.setVisibility(View.GONE);





        }
    }
    /*
     * JavaScript Interface. Web code can access methods in here
     * (as long as they have the @JavascriptInterface annotation)
     */
    public class WebViewJavaScriptInterface{

        private Context context;
        private NewActivity webview;

        /*
         * Need a reference to the context in order to sent a post message
         */
        public WebViewJavaScriptInterface(Context context,NewActivity activity){
            this.context = context;
            this.webview = activity;
        }

        /*
         * This method can be called from Android. @JavascriptInterface
         * required after SDK version 17.
         */
        @JavascriptInterface
        public void makeToast(String message, boolean lengthLong){
            Toast.makeText(context, message, (lengthLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT)).show();
        }
        @JavascriptInterface
        public void CambioHoja(String Hoja ){
           webview.HojaActual=Hoja;
          webview.txtNumeroPagina.setText(Hoja.toString());
        }
        @JavascriptInterface
        public void TotalHojas(int paramInt)
        {
          this.webview.TotalPaginas = paramInt;
        }
      @JavascriptInterface
      public void GuardarRayado(String Hoja, String Data) {
      //  SQLiteDatabase bd = new AdminSQLiteOpenHelper(getApplicationContext(), "libros", null, 1).getWritableDatabase();
        ContentValues registro;
        if (bd.rawQuery("SELECT DATA FROM RAYADO WHERE HOJA='" + Hoja + "' and LIBROID=" + webview.libroid, null).moveToFirst()) {
          registro = new ContentValues();
          registro.put("DATA", Data);
          bd.update("RAYADO", registro, "HOJA='" + Hoja + "' AND LIBROID=" + webview.libroid, null);
        } else {
          registro = new ContentValues();
          registro.put("LIBROID", Integer.valueOf(webview.libroid));
          registro.put("HOJA", Hoja);
          registro.put("DATA", Data);
          bd.insert("RAYADO", null, registro);
        }

       // bd.close();
      }
      @JavascriptInterface
      public void GuardarSubrayado(String Hoja, String Data) {
       // SQLiteDatabase bd = new AdminSQLiteOpenHelper(getApplicationContext(), "libros", null, 1).getWritableDatabase();
        ContentValues registro;
        Cursor fila=bd.rawQuery("SELECT DATA FROM SUBRAYADO WHERE HOJA='" + Hoja + "' and LIBROID=" + webview.libroid, null);
        if (fila.moveToFirst()) {
          registro = new ContentValues();
          registro.put("DATA", Data);
          bd.update("SUBRAYADO", registro, "HOJA='" + Hoja + "' AND LIBROID=" + webview.libroid, null);
        } else {
          registro = new ContentValues();
          registro.put("LIBROID", Integer.valueOf(webview.libroid));
          registro.put("HOJA", Hoja);
          registro.put("DATA", Data);
          bd.insert("SUBRAYADO", null, registro);
        }
        fila.close();
       // bd.close();
      }
        @JavascriptInterface
        public void cerrarEjercicio() {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    webview.esconder();
                    // Stuff that updates the UI

                }
            });

        }
        @JavascriptInterface
        public void InsertarEscribir (String ejercicio,String datos,String id) {

            // SQLiteDatabase bd = new AdminSQLiteOpenHelper(getApplicationContext(), "libros", null, 1).getWritableDatabase();
            ContentValues registro;
            Cursor fila=bd.rawQuery("SELECT DATA FROM ESCRIBIR WHERE LIBROID=" + webview.libroid + " and EJERCICIO='" + ejercicio + "' and ELEMENTO='" + id+ "'" , null);

            if (fila.moveToFirst()) {
                registro = new ContentValues();
                registro.put("DATA", datos);
                bd.update("ESCRIBIR", registro, "LIBROID=" + webview.libroid + " and EJERCICIO='" + ejercicio + "' and ELEMENTO='" + id+ "'", null);
            } else {
                registro = new ContentValues();
                registro.put("LIBROID", Integer.valueOf(webview.libroid));
                registro.put("EJERCICIO", ejercicio);
                registro.put("DATA", datos);
                registro.put("ELEMENTO", id);
                registro.put("ESTADO", 2);

                bd.insert("ESCRIBIR", null, registro);
            }
            fila.close();
            // bd.close();
        }
        @JavascriptInterface
        public void dibujarEjercicios(String ejercicio) {


            // SQLiteDatabase bd = new AdminSQLiteOpenHelper(getApplicationContext(), "libros", null, 1).getWritableDatabase();
            Cursor fila = bd.rawQuery("SELECT DATA,ELEMENTO,ESTADO FROM ESCRIBIR WHERE LIBROID=" + libroid  + " and EJERCICIO='" + ejercicio + "'"  , null);
            while (fila.moveToNext()) {
              final  String data = fila.getString(0);
                final String Elemento = fila.getString(1);
                String Estado = fila.getString(2);
                if (!data.equals("[]")) {
                    // web1.loadUrl("javascript:document.getElementById('"+ Elemento+ "').value=("+ "'"+ data + "')");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            webview.web1.loadUrl("javascript:(function() { document.getElementById('"+ Elemento+ "').value=('"+ data+ "');})()");


                        }
                    });




                    System.out.println("javascript: document.getElementById('"+ Elemento+ "').value=("+ "'"+ data + "');");

                }
                if(!(Estado.equals( "2")))
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            webview.web1.loadUrl("javascript:(function() { document.getElementById('"+ Elemento+ "').disabled=true;})()");


                        }
                    });


                    if(Estado.equals( "1"))
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                webview.web1.loadUrl("javascript:(function() { document.getElementById('bien_"+ Elemento+ "').style.color='green';})()");


                            }
                        });


                    }
                   else if(Estado.equals( "0"))
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                webview.web1.loadUrl("javascript:(function() { document.getElementById('mal_"+ Elemento+ "').style.color='orange';})()");


                            }
                        });


                    }

            }
            }
            fila.close();
            //  bd.close();
        }

        @JavascriptInterface
        public void calificarEjercicios(int estado,String ejercicio,String id)  {
            ContentValues registro;
            Cursor fila=bd.rawQuery("SELECT DATA FROM ESCRIBIR WHERE LIBROID=" + webview.libroid + " and EJERCICIO='" + ejercicio + "' and ELEMENTO='" + id+ "'" , null);

            if (fila.moveToFirst()) {
                registro = new ContentValues();
                registro.put("ESTADO", estado);
                bd.update("ESCRIBIR", registro, "LIBROID=" + webview.libroid + " and EJERCICIO='" + ejercicio + "' and ELEMENTO='" + id+ "'", null);
            } else {
                registro = new ContentValues();
                registro.put("LIBROID", Integer.valueOf(webview.libroid));
                registro.put("EJERCICIO", ejercicio);
                registro.put("ELEMENTO", id);
                registro.put("ESTADO", estado);

                bd.insert("ESCRIBIR", null, registro);
            }
            fila.close();
            // bd.close();
        }

      @JavascriptInterface
      public void ShowIndice(String jsonString)  {
         webview.Capitulos=jsonString;
      }

      @JavascriptInterface
      public void ActivarTap() {
        this.webview.FavoritosActivo = Boolean.valueOf(false);
      }

        @JavascriptInterface
        public void TomarFoto(String img,String paginaid) {

            checarpermisocamara(img,paginaid,0);
        }

        @JavascriptInterface
        public void EliminarFoto(String img,String paginaid) {
            borrarFotoAdentroLibro(img, paginaid);
        }
        @JavascriptInterface
        public void cambiarCandado(String accion) {

            if(accion.equals("abrir")){
                webview.ocultarbotoncandado(accion);

            }
            else if(accion.equals("cerrar")){

                webview.ocultarbotoncandado(accion);
            }
        }

    }

}
