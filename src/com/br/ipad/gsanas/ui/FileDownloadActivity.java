package com.br.ipad.gsanas.ui;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.br.ipad.gsanas.connetion.WebServerConnection;
import com.br.ipad.gsanas.facade.Facade;
import com.br.ipad.gsanas.facade.FacadeWebServer;
import com.br.ipad.gsanas.repository.BasicRepository;
import com.br.ipad.gsanas.util.Constants;
import com.br.ipad.gsanas.util.Util;

public class FileDownloadActivity extends Activity implements OnClickListener {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Facade.setContext( this );
        boolean databaseExists = BasicRepository.checkDataBase();
        
        if ( !databaseExists ){

        	FacadeWebServer web =  FacadeWebServer.getInstancia(FileDownloadActivity.this);
        	
        	if ( web.isServerOnline() ){ 
                ProgressDialog mProgressDialog = ProgressDialog.show(this, getString(R.string.labelFileDownload),getString(R.string.labelFileDownloadLoading),false,true);
                
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setMax(100);
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                
                AsyncTask<Object, Object, Integer> taskDownloadFile = new AsyncTask<Object, Object, Integer>(){
                	
					@Override
					protected Integer doInBackground(Object... params) {
						
						try{
					    	FacadeWebServer downloadFile =  FacadeWebServer.getInstancia(FileDownloadActivity.this);
	                        
	                        int sucess = downloadFile.downloadFile();
	                        
	                        if ( sucess == WebServerConnection.OK ){
	                        	
	                        	File directory = new File(Constants.SDCARD_PATH);                        	
	                        	Util.deletePhotoFolder( directory );
	                        	
	                			Intent i = new Intent(FileDownloadActivity.this, LoginActivity.class);
	                			FileDownloadActivity.this.startActivity(i);
	                        }
	                        
	                        return sucess;
						} catch ( Exception e ){
							Log.e( Constants.CATEGORY, e.getMessage());
							e.printStackTrace();
							return WebServerConnection.ERROR_GENERIC;
						}
					}
					
					protected void onPostExecute(Integer sucess) {
						
				    	String errorMsg = null;
				    	
				    	switch (sucess){
				    	
				    		case WebServerConnection.ERROR_GENERIC:
				    			errorMsg = "Houve um problema desconhecido carregar os dados. Encaminhando para carregamento OFFLINE";
				    			break;
				    			
				    		case WebServerConnection.ERROR_DOWNLOAD_FILE:
				    			errorMsg = "Houve um problema ao baixar o arquivo. Encaminhando para carregamento OFFLINE";
				    			break;
				    			
				    		case WebServerConnection.ERROR_LOADING_FILE:
				    			errorMsg = "Houve um problema ao carregar o arquivo. Encaminhando para carregamento OFFLINE";
				    			break;
				    			
				    		case WebServerConnection.ERROR_SERVER_OFF_LINE:
				    			errorMsg = "O servidor está OFFLINE. Encaminhando para carregamento OFFLINE";
				    			break;
				    			
				    		case WebServerConnection.ERROR_ROUTE_INITIALIZATION_SIGNAL:
			    				errorMsg = "Houve um erro ao tentar marcar o arquivo para EM CAMPO. Encaminhando para carregamento OFFLINE";
			    				break;
			    				
				    		case WebServerConnection.ERROR_NO_FILE:
			    				errorMsg = "Nenhum arquivo liberado para o celular. Encaminhando para carregamento OFFLINE";
			    				break;


				    	}
				    	
				    	if ( errorMsg != null ){	
				    		
				    		File directory = new File(Constants.SDCARD_PATH);                        	
			            	Util.deletePhotoFolder( directory );
				    		
                        	BasicRepository.getInstance().deleteDatabase();
				    		
			        		new AlertDialog.Builder(FileDownloadActivity.this)
			    			.setTitle("Erro ao carregar arquivo")
			    			.setMessage( errorMsg )
			    			.setNeutralButton(Constants.ALERT_OK, 
			    				new DialogInterface.OnClickListener() {
			    					public void onClick(
			    						DialogInterface dialog,
			    						int which) {
			    						// Caso contrario, encaminhamos para a tela de carregamento de arquivo offline
			    						Intent i = new Intent(FileDownloadActivity.this, FileSelector.class);
			    						FileDownloadActivity.this.startActivity(i);
			    					}
			    			}).show();
				    	}
					}
                };
                
        		taskDownloadFile.execute();
        	} else {
        		new AlertDialog.Builder(FileDownloadActivity.this)
    			.setTitle("Servidor fora do ar")
    			.setMessage("Não foi possivel conectar o servidor GSAN. Encaminhando para trabalho OFFLINE")
    			.setNeutralButton(Constants.ALERT_OK, 
    				new DialogInterface.OnClickListener() {
    					public void onClick(
    						DialogInterface dialog,
    						int which) {
    						// Caso contrario, encaminhamos para a tela de carregamento de arquivo offline
    						Intent i = new Intent(FileDownloadActivity.this, FileSelector.class);
    						FileDownloadActivity.this.startActivity(i);
    					}
    			}).show();
        	}
        	
        	
        	
        } else {
        	Intent i = new Intent(this, LoginActivity.class);
    		startActivity(i);        	
        }
        
    }	

	public void onClick(View arg0) {
		
	}
}
