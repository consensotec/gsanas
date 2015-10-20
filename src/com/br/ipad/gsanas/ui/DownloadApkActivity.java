package com.br.ipad.gsanas.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;

import com.br.ipad.gsanas.connetion.WebServerConnection;
import com.br.ipad.gsanas.facade.Facade;
import com.br.ipad.gsanas.repository.BasicRepository;
import com.br.ipad.gsanas.util.Constants;
import com.br.ipad.gsanas.util.Util;

public class DownloadApkActivity extends Activity {	

	private static String VERSAO_BAIXADA = "";
	
	private static final char RESPOSTA_OK = '*';

	private static final int ERROR_ABORT_REQUESTED = 99;
	public static final int ERRO_VERSAO_INDISPONIVEL = 98;
	public static final int IGNORE_DOWNLOAD_VERSION = 90;
	private static final int ERRO_VERIFICANDO_VERSAO = 97;
	private static final int ERRO_DOWNLOAD_APK = 96;
	private static final int VERSAO_IGUAL = 95;
	
	private class DownloadApkControl extends AsyncTask<ProgressBar, Integer, Integer>{

		private ProgressBar prbRoute;
		protected boolean abort = false;
		
    /**
     * Prepare activity before upload
     */
	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        File filesDirGsanasRoot = new File( Constants.SDCARD_PATH );
			 if(!filesDirGsanasRoot.exists()){					 
				 filesDirGsanasRoot.mkdirs();
			 }
			 
	        File filesDir = new File( Constants.VERSION_PATH );
			 if(!filesDir.exists()){					 
				 filesDir.mkdirs();
			 }
	    }

	    @Override
	    protected void onPostExecute(Integer sucess) {	    	
	    	if(rdc.abort == false){
		    	String errorMsg = DownloadApkActivity.this.verificaErros(sucess);
		    	
		    	if ( errorMsg != null ){	
	//				File directory = new File(Constants.SDCARD_PATH);                        	
	//				Util.deletePhotoFolder( directory );
	//				
	//		    	BasicRepository.getInstance().deleteDatabase();
			
			    	new AlertDialog.Builder(DownloadApkActivity.this)
					.setTitle(getString(R.string.str_alert_download_title))
					.setMessage( errorMsg )
					.setNeutralButton(Constants.ALERT_OK, 
						new DialogInterface.OnClickListener() {
							public void onClick(
								DialogInterface dialog,
								int which) {
								Intent i = new Intent(DownloadApkActivity.this, RouteDownloadActivity.class);
								DownloadApkActivity.this.startActivity(i);
							}
					}).show();
				} else {							
					String appName = Constants.VERSION_PATH + "/"+ Constants.APK_NAME;
	                Intent intent = new Intent(Intent.ACTION_VIEW); 
	                intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
	                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	                intent.setDataAndType(Uri.fromFile(new File(appName)),
	                "application/vnd.android.package-archive");
	                
	                DownloadApkActivity.this.startActivity(intent);
	                
				}
	    	}
	    }	 
	    	    
	    @Override
	    protected void onProgressUpdate(Integer... values) {
	    	prbRoute.setProgress( values[0] );
	    }	
	    
	    private int	baixarApk(InputStream in, int fileSize){			

			File file = new File(Constants.VERSION_PATH+"/"+Constants.APK_NAME);
			
			try{	    	

   			 prbRoute.setMax( fileSize );
				
				FileOutputStream fos = new FileOutputStream(file);
		       
				// Guarda o valor do primeiro byte de resposta
				// para depois verificar se o download foi feito
		        String valor = Util.getValorRespostaInputStream(in);
		        
		        int progress = 0;
		       
                byte[] buffer = new byte[in.available()];
                int len;
                if( valor.equals("*")){
                    while ((len = in.read(buffer)) != -1) {
                    	progress += len;
		    	  		publishProgress( progress );
		    	  		fos.write(buffer,0,len);
		    	  		// Por último, escreve o valor do byte resposta 
			           
			           
                    }
                    fos.write((byte)RESPOSTA_OK);
                    fos.flush();
		            fos.close();
		            
		        }else{
		        	return ERROR_ABORT_REQUESTED;
		        }
				} catch (FileNotFoundException e) {
					Log.e( Constants.CATEGORY , e.getMessage());
					e.printStackTrace();
				} catch (IOException e) {
					Log.e( Constants.CATEGORY , e.getMessage());
					e.printStackTrace();
				}
			return WebServerConnection.OK;
		}
	
		@Override
		protected Integer doInBackground(ProgressBar... params) {
			prbRoute = params[0];	
			
			WebServerConnection downloadFile = new WebServerConnection( DownloadApkActivity.this ); 
			   	    	
			try{
	    	
				ArrayList<Object> parametros = new ArrayList<Object>(2);
				parametros.add( new Byte( WebServerConnection.DOWNLOAD_APK ) );
				parametros.add( new Long( Util.getIMEI( DownloadApkActivity.this ) ) );
				
				InputStream in = downloadFile.communicate( Constants.ACTION, parametros );
			
				baixarApk(in,downloadFile.getFileLength());
				
			} catch (MalformedURLException e) {
				e.printStackTrace();
				Log.e( Constants.CATEGORY , e.getMessage());
				return WebServerConnection.ERROR_GENERIC;
			} catch (IOException e) {
				e.printStackTrace();
				Log.e( Constants.CATEGORY , e.getMessage());
				return WebServerConnection.ERROR_LOADING_FILE;
			} catch ( Exception e ){
				e.printStackTrace();
				Log.e( Constants.CATEGORY , e.getMessage());
				return WebServerConnection.ERROR_LOADING_FILE;
			}			
			
			return WebServerConnection.OK;
		}		
	
	}	
	
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	// TODO Auto-generated method stub
    	super.onActivityResult(requestCode, resultCode, data);
    	// Verifica se a foto foi salva
		if (requestCode == Constants.INSTALL_APK) {

			//Se não clicou em 'abrir'
			if (resultCode != RESULT_OK) {
				new AlertDialog.Builder(DownloadApkActivity.this)
    			.setTitle(getString(R.string.str_download_apk_alert_error))
    			.setMessage( getString(R.string.str_download_apk_aborted) )
    			.setNeutralButton(Constants.ALERT_OK, 
    				new DialogInterface.OnClickListener() {
    					public void onClick(
    						DialogInterface dialog,
    						int which) {
    						
    						Intent i = new Intent(DownloadApkActivity.this, DownloadApkActivity.class);
    						DownloadApkActivity.this.startActivity(i);
    					
    					}
    			}).show(); 
			}else{

	            Constants.VERSION = VERSAO_BAIXADA;
			}
			
		}
    }
	
	private Button btnCancel;
		
	private DownloadApkControl rdc;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_apk_activity);
        //Facade.setContext( this );
        
        btnCancel = ( Button ) findViewById(R.id.btnCancelRDA);
        
        btnCancel.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				
				if(rdc != null){
					rdc.abort = true;
				}
				new AlertDialog.Builder(DownloadApkActivity.this)
    			.setTitle(getString(R.string.str_download_apk_alert_error))
    			.setMessage( getString(R.string.str_download_apk_aborted) )
    			.setNeutralButton(Constants.ALERT_OK, 
    				new DialogInterface.OnClickListener() {
    					public void onClick(
    						DialogInterface dialog,
    						int which) {
    						
    						Intent i = new Intent(DownloadApkActivity.this, DownloadApkActivity.class);
    						startActivity(i);
    					
    					}
    			}).show(); 
				
			} 
		});
        
        boolean databaseExists = BasicRepository.checkDataBase();
        
        int sucess = IGNORE_DOWNLOAD_VERSION;
        if(!databaseExists){
        	sucess = compareVersions();
        }
        	
        if(sucess == WebServerConnection.OK) {
    		startDownload();        	
        }else{

        	String errorMsg = DownloadApkActivity.this.verificaErros(sucess);   
 	    	
 	    	if ( errorMsg != null ){	
 	            
 	            if ( databaseExists ){ 
 	            	new AlertDialog.Builder(DownloadApkActivity.this)
 	     			.setTitle(getString(R.string.str_download_apk_alert_error))
 	     			.setMessage( errorMsg )
 	     			.setNeutralButton(Constants.ALERT_OK, 
 	     				new DialogInterface.OnClickListener() {
 	     					public void onClick(
 	     						DialogInterface dialog,
 	     						int which) {

 	     						Intent i = new Intent(DownloadApkActivity.this, LoginActivity.class);
 	     						DownloadApkActivity.this.startActivity(i);
 	     					}
 	     			}).show();
 	            }else{
	
	             	new AlertDialog.Builder(DownloadApkActivity.this)
	     			.setTitle(getString(R.string.str_download_apk_alert_error))
	     			.setMessage( errorMsg )
	     			.setNeutralButton(Constants.ALERT_OK, 
	     				new DialogInterface.OnClickListener() {
	     					public void onClick(
	     						DialogInterface dialog,
	     						int which) {
	     						// Caso contrario, encaminhamos para a tela de carregamento de arquivo offline
	     						Intent i = new Intent(DownloadApkActivity.this, RouteDownloadActivity.class);
	     						DownloadApkActivity.this.startActivity(i);
	     					}
	     			}).show();
 	            }
				
             }else{
            	 	// Caso contrario, encaminhamos para a tela de carregamento de arquivo offline
					Intent i = new Intent(DownloadApkActivity.this, RouteDownloadActivity.class);
					DownloadApkActivity.this.startActivity(i);
             }
        }
    }
    
    private void startDownload() {
		ProgressBar progress = (ProgressBar) findViewById( R.id.progressByLine );
		progress.setIndeterminate( false );
		rdc = new DownloadApkControl();
		rdc.execute( progress );		
	}
    
    private int compareVersions() {		
		
		WebServerConnection downloadFile = new WebServerConnection( DownloadApkActivity.this );
		
		int sucess = WebServerConnection.ERROR_GENERIC;
		
		boolean serverOnline = downloadFile.isServerOnline();
		
		if ( serverOnline ){	
			ArrayList<Object> parametros = new ArrayList<Object>(2);
			parametros.add( new Byte( WebServerConnection.VERIFICAR_VERSAO ) );
			parametros.add( new Long( Util.getIMEI( DownloadApkActivity.this ) ) );
			
			String currentVersion = Constants.VERSION;
			Integer currentVersionNumber = Integer.valueOf(currentVersion.replace(".",""));
			
			try {
				InputStream in = downloadFile.communicate( Constants.ACTION, parametros );
			
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
	    	    StringBuilder sb = new StringBuilder();
	    	    String line = null;
	    	    String newVersion = null;
	    	    Integer newVersionNumber = null;
	    	   
				while ((line = reader.readLine()) != null) { 
				  sb.append(line + "\n");
				}
				if(sb.length() != 0){
					newVersion = sb.substring(1,sb.length()-1);
					if(newVersion != ""){
						newVersion = newVersion.replace(".", "");
						newVersionNumber = Integer.valueOf(newVersion);
					}
				}
					
				if(newVersionNumber != null){
					
					if(newVersionNumber > currentVersionNumber){
						VERSAO_BAIXADA = newVersion;
						sucess= WebServerConnection.OK;			
					}else{
					// Ignora o download
					sucess= IGNORE_DOWNLOAD_VERSION;		
				}
				}else{
					// Ignora o download
					sucess= IGNORE_DOWNLOAD_VERSION;		
				}
				
				} catch (IOException e) {
					Log.e( Constants.CATEGORY , e.getMessage());
					e.printStackTrace();
					return IGNORE_DOWNLOAD_VERSION;
				}
		}else{
			sucess= WebServerConnection.ERROR_SERVER_OFF_LINE;
		}

		return sucess;
		
    }

	private String verificaErros(Integer sucess) {
		String errorMsg = null;
		
		switch (sucess){ 
    	
			case WebServerConnection.ERROR_GENERIC:
				errorMsg = getString(R.string.str_error_generic);
				break;
				
			case WebServerConnection.ERROR_DOWNLOAD_FILE:
				errorMsg = getString(R.string.str_error_download);
				break;				
				
			case WebServerConnection.ERROR_LOADING_FILE:
				errorMsg = getString(R.string.str_error_laoding);
				break;			
				
			case WebServerConnection.ERROR_SERVER_OFF_LINE:
				errorMsg = getString(R.string.str_error_off);
				break;	    			
				
			case WebServerConnection.ERROR_NO_FILE:
				errorMsg = getString(R.string.str_error_nofile);
				break;
				
			case VERSAO_IGUAL:
				//errorMsg = getString(R.string.str_alert_mesma_versao);
				break;	
				
			case ERRO_DOWNLOAD_APK:
				errorMsg = getString(R.string.str_alert_download_apk_erro);
				break;
				
			case ERRO_VERIFICANDO_VERSAO:
				errorMsg = getString(R.string.str_alert_verificar_versao_erro);
				break;

			case ERRO_VERSAO_INDISPONIVEL:
				errorMsg = getString(R.string.str_alert_download_download_error);
				break;
				
			case ERROR_ABORT_REQUESTED:
				errorMsg = getString(R.string.str_download_apk_aborted);
				break;
		}
		
		if(errorMsg != null){
			return errorMsg;
		}
		
		return null;		
		
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		
		// Caso o usuário tenha apertado em cancelar ao baixar a versão
    	new AlertDialog.Builder(DownloadApkActivity.this)
		.setTitle(getString(R.string.str_download_apk_alert_error))
		.setMessage( getString(R.string.str_download_apk_aborted) )
		.setNeutralButton(Constants.ALERT_OK, 
			new DialogInterface.OnClickListener() {
				public void onClick(
					DialogInterface dialog,
					int which) {
					
					Intent i = new Intent(DownloadApkActivity.this, DownloadApkActivity.class);
					startActivity(i);
					finish();
				
				}
		}).show(); 
	}
	
	@Override
	public void onAttachedToWindow()
	{  
		this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);     
		super.onAttachedToWindow();  
	}
	
}