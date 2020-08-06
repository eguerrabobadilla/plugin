package com.moduscreate.plugin;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaInterface;

public class ModusEcho extends CordovaPlugin {
  public void initialize(CordovaInterface cordova, CordovaWebView webView) {
      super.initialize(cordova, webView);
  }

  @Override
  public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    Context context = this.cordova.getActivity().getApplicationContext();
    if ("echo".equals(action)) {
      JSONArray jObject = new JSONArray(args.getString(0));
      echo(jObject.get(0).toString(), jObject.get(1).toString(),jObject.get(2).toString(), callbackContext, context);
      return true;
    } else if (!action.equals("new_activity")) {
      return false;
    } else {
      openNewActivity(context);
      return true;
    }
  }

  /*private void echo(String msg, CallbackContext callbackContext) {
    if (msg == null || msg.length() == 0) {
      callbackContext.error("Empty message!");
    } else {
      Toast.makeText(webView.getContext(),msg,Toast.LENGTH_LONG).show();
      callbackContext.success(msg);
    }
  }*/
  private void echo(String path, String libroid,String app, CallbackContext callbackContext, Context context) {
    Intent intent = new Intent(context, NewActivity.class);
    intent.putExtra("path", path);
    intent.putExtra("libroid", libroid);
    intent.putExtra("app", app);
    int a= Build.VERSION.SDK_INT;
    if(a>19) {
      this.cordova.getActivity().finish();
    }
    this.cordova.getActivity().startActivity(intent);
  }

  private void openNewActivity(Context context) {
    this.cordova.getActivity().startActivity(new Intent(context, NewActivity.class));
  }
}
