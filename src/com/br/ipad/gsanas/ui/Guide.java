/*
* Copyright (C) 2007-2007 the GSAN - Sistema Integrado de Gestão de Serviços de Saneamento
*
* This file is part of GSAN, an integrated service management system for Sanitation
*
* GSAN is free software; you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation; either version 2 of the License.
*
* GSAN is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA
*/

/*
* GSAN - Sistema Integrado de Gestão de Serviços de Saneamento
* Copyright (C) <2007> 
* Adriano Britto Siqueira
* Alexandre Santos Cabral
* Ana Carolina Alves Breda
* Ana Maria Andrade Cavalcante
* Aryed Lins de Araújo
* Bruno Leonardo Rodrigues Barros
* Carlos Elmano Rodrigues Ferreira
* Cláudio de Andrade Lira
* Denys Guimarães Guenes Tavares
* Eduardo Breckenfeld da Rosa Borges
* Fabíola Gomes de Araújo
* Fernanda Vieira de Barros Almeida
* Flávio Leonardo Cavalcanti Cordeiro
* Francisco do Nascimento Júnior
* Homero Sampaio Cavalcanti
* Ivan Sérgio da Silva Júnior
* José Edmar de Siqueira
* José Thiago Tenório Lopes
* Kássia Regina Silvestre de Albuquerque
* Leonardo Luiz Vieira da Silva
* Márcio Roberto Batista da Silva
* Maria de Fátima Sampaio Leite
* Micaela Maria Coelho de Araújo
* Nelson Mendonça de Carvalho
* Newton Morais e Silva
* Pedro Alexandre Santos da Silva Filho
* Rafael Corrêa Lima e Silva
* Rafael Francisco Pinto
* Rafael Koury Monteiro
* Rafael Palermo de Araújo
* Raphael Veras Rossiter
* Roberto Sobreira Barbalho
* Rodrigo Avellar Silveira
* Rosana Carvalho Barbosa
* Sávio Luiz de Andrade Cavalcante
* Tai Mu Shih
* Thiago Augusto Souza do Nascimento
* Thúlio dos Santos Lins de Araújo
* Tiago Moreno Rodrigues
* Vivianne Barbosa Sousa
*
* Este programa é software livre; você pode redistribuí-lo e/ou
* modificá-lo sob os termos de Licença Pública Geral GNU, conforme
* publicada pela Free Software Foundation; versão 2 da
* Licença.
* Este programa é distribuído na expectativa de ser útil, mas SEM
* QUALQUER GARANTIA; sem mesmo a garantia implícita de
* COMERCIALIZAÇÃO ou de ADEQUAÇÃO A QUALQUER PROPÓSITO EM
* PARTICULAR. Consulte a Licença Pública Geral GNU para obter mais
* detalhes.
* Você deve ter recebido uma cópia da Licença Pública Geral GNU
* junto com este programa; se não, escreva para Free Software
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
* 02111-1307, USA.
*/  

package com.br.ipad.gsanas.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.br.ipad.gsanas.adapters.GuideAdapter;
import com.br.ipad.gsanas.connetion.PhotoConnection;
import com.br.ipad.gsanas.exception.BusinessException;
import com.br.ipad.gsanas.exception.FacadeException;
import com.br.ipad.gsanas.facade.Facade;
import com.br.ipad.gsanas.model.Activit;
import com.br.ipad.gsanas.model.OSActvExecutedPeriod;
import com.br.ipad.gsanas.model.Photo;
import com.br.ipad.gsanas.model.PhotoType;
import com.br.ipad.gsanas.model.ServiceOrder;
import com.br.ipad.gsanas.model.ServiceOrder.ServiceOrders;
import com.br.ipad.gsanas.model.ServiceOrderActivity;
import com.br.ipad.gsanas.model.ServiceOrderSituation;
import com.br.ipad.gsanas.repository.BasicRepository;
import com.br.ipad.gsanas.util.Constants;
import com.br.ipad.gsanas.util.ExportBancoDados;
import com.br.ipad.gsanas.util.Util;

public class Guide extends Activity implements LocationListener {
		
	private GuideAdapter adapter;
	private Intent i;
	private ListView lv;	
	int total;
	private Button btMenu;
	
	private ServiceOrder serviceOrder;
	private ArrayList<ServiceOrder> serviceOrderList;
	
	private int aux;
	
	private Facade facade= Facade.getInstance();
	
	private static final int repeatTime = 120 * 1000;
	private static AlarmManager alarm;
	private static PendingIntent p;
	
	protected LocationManager locationManager;
	private Double latitude;
	private Double longitude;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, Constants.GPS_PERIOD, 500, this);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guide);
        Facade.setContext(this);
        
        startServices();
        
		lv = (ListView) findViewById(R.id.list);	
					
		lv.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				serviceOrder = (ServiceOrder)view.getTag();
								
				i = new Intent(Guide.this, ObservationActivity.class);
				i.putExtra("serviceOrder", serviceOrder);
				startActivity(i);
				
				return true;
			}
		});
					
		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long id) {
				
				Date programmingDate = null;
				try {
					//Pergunta se quer que os dados sejam deletados, caso a data de programação seja diferente da atual
					programmingDate = facade.getFirstServiceOrderDate();
					
				} catch (FacadeException e) {
					e.printStackTrace();
					Log.e( Constants.CATEGORY , e.getMessage());
				}
				if ( programmingDate != null &&
						!Util.isEqualsDates(programmingDate, Util.getCurrentDateTime()) ){
				
					new AlertDialog.Builder(Guide.this)
	    			.setMessage(getString(R.string.str_guide_wrong_date,Util.formatDate(programmingDate),Util.formatDate(Util.getCurrentDateTime())))
	    			.setPositiveButton(getString(R.string.sim), 
	    				new DialogInterface.OnClickListener() {
	    					public void onClick(DialogInterface dialog,int which) {
	    						// finaliza o alarm
	    		    			Util.cancelaAlarmes();
	    		        		
	    						File directory = new File(Constants.SDCARD_PATH);                        	
	    			    		Util.deletePhotoFolder( directory );
	    			    		
	    			        	BasicRepository.getInstance().deleteDatabase();
	    			        	
	    			        	Intent i = new Intent(Guide.this, RouteDownloadActivity.class);
	    						Guide.this.startActivity(i);
	    			        	
	    					}
	    			
	    			})
	    			.setNegativeButton(getString(R.string.nao), 
		    				new DialogInterface.OnClickListener() {
		    					public void onClick(DialogInterface dialog,int which) {
		    							
		    					}
	    			}).
	    			show();
				}else{
						
					//pega as OSs em execucao				
					try {
						serviceOrderList = facade.getServiceOrderSituation();
					} catch (FacadeException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					serviceOrder = (ServiceOrder)view.getTag();
					
					// Verifica se a OS está para ser iniciada
					if(serviceOrder.getServiceOrderSituation().getId().intValue() == ServiceOrderSituation.SITUATION_TOSTART){
		
						// Verifica se ja existe alguma OS em execucao
						if(serviceOrderList.size() == 0){
							
							Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);   			
	
							startActivityForResult(intent, Constants.CAMERA_TAKE_PICTURE);
						
						}else{
							AlertDialog.Builder alert = new AlertDialog.Builder(Guide.this);
							alert.setMessage(getString(R.string.str_guide_alert));
							alert.setNeutralButton(Constants.ALERT_OK, new DialogInterface.OnClickListener() {
								
								public void onClick(DialogInterface arg0, int arg1) {
									
								}
							});
							alert.show();
						}
					}else if(serviceOrder.getServiceOrderSituation().getId() == ServiceOrderSituation.SITUATION_FINISHED){
						AlertDialog.Builder alert = new AlertDialog.Builder(Guide.this);
						alert.setMessage(getString(R.string.str_guide_alert_finished));
						alert.setNeutralButton(Constants.ALERT_OK, new DialogInterface.OnClickListener() {
							
							public void onClick(DialogInterface arg0, int arg1) {
								
							}
						});
						alert.show();
						
					}else{
						i = new Intent(Guide.this, ServiceOrderActvity.class);
						i.putExtra("editar", serviceOrder);	
						startActivity(i); 
					}
				}			
			}
       });		       
	
		btMenu = (Button) findViewById(R.id.btMenu);
		btMenu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				openOptionsMenu();
			}
		});
		
	}
	
	private void startServices() {
		// Seta o período dos alarmes
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());
		c.add(Calendar.SECOND, 1);

		long time = c.getTimeInMillis();
		
		// Inicia o alarme de comunicação
		if(alarm == null){
			i = new Intent("COMMUNICATION_SERVICE");
			p = PendingIntent.getService(Guide.this, 2, i, 0);
			
			alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
			alarm.setRepeating(AlarmManager.RTC_WAKEUP, time,repeatTime, p);
		}
		
		
	}

	protected void insertServiceOrderActivity() {
		try {
			ServiceOrderActivity sOActivityOld = facade.getServiceOrderActivityByFk(Constants.UNIQUE_ACTIVITY, serviceOrder.getId());
			if(sOActivityOld == null){
				Activit actvity = facade.getActivityById(1);
				ServiceOrderActivity sOactivity = new ServiceOrderActivity();
				sOactivity.setActivity(actvity);
				sOactivity.setServiceOrder(serviceOrder);
				sOactivity.setExcluded(Constants.NO.toString());
				facade.insertServiceOrderActivity(sOactivity);
				
				//Pega a ServiceOrderActivity que acabou de ser inserida
				ServiceOrderActivity sOActivityNew = facade.getServiceOrderActivityByFk(Constants.UNIQUE_ACTIVITY, serviceOrder.getId());
				
				OSActvExecutedPeriod oSActvExecutedPeriod = new OSActvExecutedPeriod();
				oSActvExecutedPeriod.setServOrderActvity(sOActivityNew);
				oSActvExecutedPeriod.setRunningStart(Util.getCurrentDateTime());
				
				facade.insertOSActvExecutedPeriod(oSActvExecutedPeriod);
			}
			
		} catch (FacadeException e) {
			e.printStackTrace();
			Log.e( Constants.CATEGORY , e.getMessage());
		} catch (BusinessException e) {
			e.printStackTrace();
			Log.e( Constants.CATEGORY , e.getMessage());
		}
	}

	@Override
    protected void onResume() {
    	
    	super.onResume();
    	
    		try {
				adapter = new GuideAdapter(Guide.this,facade.getAllServiceOrder(ServiceOrders.SEQUENTIALPROGRAMMING));
				total = adapter.getCount();
			} catch (FacadeException e) {
				
				Log.e( Constants.CATEGORY , e.getMessage());
			} 
    		lv.setAdapter(adapter);    	       
    }
	
	 @Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			
			// Verifica se a foto foi salva
			if (data != null && requestCode == Constants.CAMERA_TAKE_PICTURE) {
				
				if (resultCode == RESULT_OK) {
					
					insertServiceOrderActivity();
					
					//altera a situação da OS
					ServiceOrderSituation servOrderSituation = new ServiceOrderSituation();
					servOrderSituation.setId(ServiceOrderSituation.SITUATION_EXECUTING);
					serviceOrder.setServiceOrderSituation(servOrderSituation, Guide.this);
					
					try {
						facade.updateServiceOrder(serviceOrder);
						
					} catch (FacadeException e) {
						e.printStackTrace();
						Log.e( Constants.CATEGORY , e.getMessage());
					} catch (BusinessException e) {
						e.printStackTrace();
						Log.e( Constants.CATEGORY , e.getMessage()); 
					}
					
					String date = Util.convertDateToDateStrFile();
					String fileName = Constants.BEGINNING + "_" + date +".jpg";
					
					//pega a foto
					Bundle bundle = data.getExtras();
					
					String imageFilePath = "/sdcard/gsanas/"+serviceOrder.getId()+"/"+Constants.BEGINNING_DIRECTORY+fileName;  
					File imageFile = new File(imageFilePath); 
					try {
						FileOutputStream out = new FileOutputStream(imageFile);
					    Bitmap bmp = (Bitmap) bundle.get("data");
					    
					    //transforma o arquivo na foto tirada
					    bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);

					    out.flush();
					    out.close();
					} catch (Exception e) {
						e.printStackTrace();
					    Log.e( Constants.CATEGORY , e.getMessage());
					}				
					 
					Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
					latitude = 0.0;
					longitude = 0.0;
					
					if(location != null){
						
						latitude = location.getLatitude();
						longitude = location.getLongitude();
					}
					
					//Seta o objeto Photo
					Photo photo = new Photo();
					photo.setPath(imageFilePath);
					PhotoType phType = new PhotoType();
					phType.setId(Constants.BEGINNING);
					photo.setPhotoType(phType);
					photo.setServiceOrder(serviceOrder);
					photo.setCordinateX(latitude);
					photo.setCordinateY(longitude);
					
					//Insere photo
					try {
						facade.insertPhoto(photo);
						photo = facade.getPhotoByPath( serviceOrder.getId(), imageFilePath );
					} catch (FacadeException e) {				
						e.printStackTrace();
						Log.e( Constants.CATEGORY , e.getMessage());
					} catch (BusinessException e) {						
						e.printStackTrace();
						Log.e( Constants.CATEGORY , e.getMessage());
					}					
					
//					PhotoConnection connection = 
//							new PhotoConnection();
					
//					connection.execute(
//							imageFile,
//							serviceOrder.getId(),
//							Util.getIMEI( Guide.this ),
//							Constants.BEGINNING,
//							photo,
//							Guide.this
//							);		
					
//					connection.execute(serviceOrder, this);

					i = new Intent(Guide.this, ServiceOrderActvity.class);
					i.putExtra("editar", serviceOrder);	
					startActivity(i);
				}
			}else{
				i = new Intent(Guide.this, Guide.class);
				startActivity(i);
			}
	 }
	 
	 @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	    	menu.add(0,Constants.FINISH_SERVICE,0,R.string.menu_guide_finish);
	    	menu.add(1,Constants.SEND_FILES,0,getString(R.string.str_menu_send_file_title));
	    	menu.add(2,Constants.EXPORTAR_BANCO,0,getString(R.string.str_menu_exportar_bd));
	    	return true;
	    }
	    
	    @Override
	    public boolean onMenuItemSelected(int featureId, MenuItem item) {
	    	
	    	switch (item.getItemId()){
	    	
	    	case Constants.FINISH_SERVICE:

				ArrayList<ServiceOrder> listSoNotFinished = null;
	    		//Lista das OSs não finalizadas
				try {
					listSoNotFinished = facade.getServiceOrderNotFinished();;
				} catch (FacadeException e) {
					e.printStackTrace();
					Log.e( Constants.CATEGORY , e.getMessage());
				}
				
	    		if(listSoNotFinished.size() != 0){
	    			AlertDialog.Builder alert = new AlertDialog.Builder(this);
	    			alert.setTitle(getString(R.string.str_login_alert_title));
	        		alert.setMessage(getString(R.string.str_error_notfinished));
	    			alert.setPositiveButton(Constants.ALERT_OK, new DialogInterface.OnClickListener() {
	    				
	    				public void onClick(DialogInterface arg0, int arg1) {
	    					
	    				}
	    			});
	    			alert.show();
				}else{
	    		
		    		final CharSequence[] items = {
		                              getString(R.string.str_guide_alert_option_on),
		                              getString(R.string.str_guide_alert_option_off)
		                      };
		    		
		    		AlertDialog.Builder builder = new AlertDialog.Builder(Guide.this);
		    		builder.setTitle(getString(R.string.str_guide_alert_finish));
		    		builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int item) {
	                        aux = item;
	                    }
	                });
		    		builder.setPositiveButton(Constants.ALERT_OK, new DialogInterface.OnClickListener() {
						 public void onClick(DialogInterface arg0, int arg1) {
							 
								                    	
		                        // Finalizar roteiro on line
		                        if (aux == 0) {
		                        			                        	
		                            Intent i = new Intent(Guide.this,
		                                                  RouteFinalizationActivity.class);
		                            startActivity(i);
		                            // Finalizar roteiro off line
		                        } else if (aux == 1) {
		                        	i = new Intent(Guide.this,RouteFinalizationActivity.class);
									i.putExtra("offline","1");
												                    	
									startActivity(i);
									
		                        }
		                    }
		                });
		    		
		    		builder.setNegativeButton(Constants.ALERT_CANCEL, new DialogInterface.OnClickListener() {
						
						public void onClick(DialogInterface arg0, int arg1) {
							
						}
					});
					AlertDialog alert = builder.create();
					alert.show();
				}
	    	break;
	    	
	    	case Constants.SEND_FILES:
	    		
	    		Intent intentSend = new Intent(Guide.this,RouteFinalizationActivity.class);
				intentSend.putExtra(getString(R.string.str_extra_send_file), "ok");
				startActivity(intentSend);
	    		break;
	    		
	    	case Constants.EXPORTAR_BANCO:
	    		
	    		
				new ExportBancoDados().exportarBancoNovoNome();
				AlertDialog.Builder alert = new AlertDialog.Builder(Guide.this);
				alert.setMessage(getString(R.string.str_alert_exportar_bd));
				alert.setCancelable(false);
				alert.setNeutralButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						return;
					}
				});
				alert.show();
	    		
	    		break;
	    	}
	    	
	    	return super.onMenuItemSelected(featureId, item); 
	    	
	    }

		
		@Override
		public void onBackPressed() {
			
		}
		
		@Override
		protected void onDestroy() {
			super.onDestroy();
			android.os.Process.killProcess(android.os.Process.myPid());	
		}

		// Objetos do serviço de comunicação
		public static PendingIntent returnPendingIntent(){
			return p;
		}

		public static AlarmManager returnAlarm() {
			return alarm;
		}
		
		public static void resetPendingIntent(){
			p = null;
		}
	    public static void resetAlarm(){
			alarm = null;
		}

	    public void onLocationChanged(Location location) {
			if(location != null){
				
				latitude = location.getLatitude();
				longitude = location.getLongitude();
			}
			
		}

		@Override
		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub
			
		}
	    
	   	
}
