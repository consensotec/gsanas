package com.br.ipad.gsanas.service;

import java.util.Date;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.br.ipad.gsanas.controller.ServiceOrderController;
import com.br.ipad.gsanas.exception.ControllerException;
import com.br.ipad.gsanas.facade.FacadeWebServer;
import com.br.ipad.gsanas.util.Constants;
import com.br.ipad.gsanas.util.Util;

/**
 * 
 * Classe responsavel pela comunicação entre o GSAN
 * e o celular. Esse serviço irá tentar se comunicar com o 
 * gsan de x em x tempo.
 * 
 * @author Bruno Barros
 * @date 05/09/2011
 *
 */
public class CommunicationService extends Service {
	
	
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		Log.i( Constants.CATEGORY, "CommunicationService.onStart()" );	
		startCommunicationService();
	}
	
	
	private void startCommunicationService() {
		new Thread(){
			public void run(){

				try {
															
					//Envia apenas se a data de programação for igual a atual
					Date programmingDate = ServiceOrderController.getInstance().getFirstServiceOrderDate();
					
					if ( programmingDate != null &&
							Util.isEqualsDates(programmingDate, Util.getCurrentDateTime()) || programmingDate == null ){

				    	FacadeWebServer downloadFile =  FacadeWebServer.getInstancia(CommunicationService.this);
					    downloadFile.updateFile();
					    //downloadFile.sendCoordinates();
					    
					    // Verificamos se algo está pendente para ser enviado
					    //downloadFile.enviarFotosNaoEnviadas();
					    downloadFile.sendAllFinishedOSInformation();					    
					}
					
				} catch (ControllerException e) {
					Log.e(Constants.CATEGORY, e.getMessage());
					e.printStackTrace();
				}
			
				stopSelf(); 
				
			}
		}.start();
		
	}


	public void onDestroy(){
		// Informamos que o serviço não está mais ativo
		Log.i( Constants.CATEGORY, "CommunicationService.onDestroy()" );
	}
	

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
}
