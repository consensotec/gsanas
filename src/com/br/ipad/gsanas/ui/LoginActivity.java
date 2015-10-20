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
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.br.ipad.gsanas.exception.FacadeException;
import com.br.ipad.gsanas.facade.Facade;
import com.br.ipad.gsanas.model.Team;
import com.br.ipad.gsanas.repository.BasicRepository;
import com.br.ipad.gsanas.util.Constants;
import com.br.ipad.gsanas.util.Util;

public class LoginActivity extends Activity {
	
	private TextView login;
	private TextView password;
	private Team team;
	private Intent i;
	private Button btLogin;
	private TextView version;
	
	//private LocationManager manager;	

		 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        Facade.setContext(this);   
            
        version = (TextView) findViewById(R.id.version);
        login = (TextView) findViewById(R.id.edtLogin);
        password = (TextView) findViewById(R.id.edtPassword);
        btLogin = (Button) findViewById(R.id.btLogin);
        version = (TextView) findViewById(R.id.version);
        
        version.setText(getString(R.string.str_login_version)+" "+ Constants.VERSION);
            	
        btLogin.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				try {
					ArrayList<Date> dates = Facade.getInstance().getFirstServiceOrderNotFinished();
			 		
					if (!dates.isEmpty()){
						
						String programmingDate = null;
						String currentDate = null;
						
						programmingDate = Util.formatDate(dates.get(0));
						currentDate = Util.formatDate(dates.get(1));											
						
			    		AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
						alert.setTitle(getString(R.string.str_login_alert_title));
			    		alert.setMessage("A data da programação " + programmingDate +
			    				" está diferente da data atual " + currentDate + ". Deseja reiniciar o processo?");
						alert.setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
							
							public void onClick(DialogInterface arg0, int arg1) {
								// Em caso de sucesso, apagamos o banco e a pasta das fotos
			                	File directory = new File(Constants.SDCARD_PATH);                        	
			                	Util.deletePhotoFolder( directory );
			                	
			                	BasicRepository.getInstance().deleteDatabase();
			                				                	
			                	Util.cancelaAlarmes();
			                	
								Intent i = new Intent( LoginActivity.this, RouteDownloadActivity.class );
								startActivity( i );
							}
						});
						alert.setNegativeButton(R.string.nao, new DialogInterface.OnClickListener() {
							
							public void onClick(DialogInterface dialog, int which) {
								AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
								alert.setTitle(getString(R.string.str_login_alert_title));
								alert.setMessage(getString(R.string.str_login_alert_wrong_date));
								alert.setNeutralButton(Constants.ALERT_OK, new DialogInterface.OnClickListener() {
									
									public void onClick(DialogInterface arg0, int arg1) {
										Intent intent = new Intent(Settings.ACTION_DATE_SETTINGS);
										startActivity(intent);
									}
								});
								alert.show();
								return;
							}
						});
						
						alert.show();
					}else{
//						try {
//							team = Facade.getInstance().validateLogin(login.getText().toString(), password.getText().toString());
//						} catch (FacadeException e) {
//							e.printStackTrace();
//							Log.e( Constants.CATEGORY , e.getMessage());
//						}
//						
//						if(team!=null){
							// Habilita gps
							String provider = Settings.Secure.getString(LoginActivity.this.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED); 
							if(!provider.contains("gps")){ 
					            final Intent poke = new Intent(); 
					            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider"); 
					            poke.addCategory(Intent.CATEGORY_ALTERNATIVE); 
					            poke.setData(Uri.parse("3"));  
					            LoginActivity.this.sendBroadcast(poke);
					        }
							
							// Ao conseguir logar, iniciamos o serviço que irá comunicar com o gsan
							// de x em x tempo.
							
														
				        	i = new Intent(LoginActivity.this,Guide.class);
				        	startActivity(i);
//				        }else{
//				        	AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
//							alert.setMessage(Constants.ALERT_MESSAGE_LOGIN);
//							alert.setNeutralButton(Constants.ALERT_OK, new DialogInterface.OnClickListener() {
//								
//								public void onClick(DialogInterface arg0, int arg1) {
//									
//								}
//							});
//							alert.show();
//				        }
					}
				} catch (FacadeException e) {
					Log.e( Constants.CATEGORY , e.getMessage());
				}				
			}
		});
        //manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);				
    }
    
    @Override
    public void onBackPressed() {

       return;
    }

//    @Override
//    protected void onResume() {
//    	super.onResume();
//    	
//    	if(!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
//			//Pergunta ao usuário para habilitar o GPS
//    		AlertDialog.Builder alert = new AlertDialog.Builder(this);
//			alert.setTitle(getString(R.string.str_login_alert_title));
//    		alert.setMessage(getString(R.string.str_login_alert_gps));
//			alert.setPositiveButton(Constants.ALERT_OK, new DialogInterface.OnClickListener() {
//				
//				public void onClick(DialogInterface arg0, int arg1) {
//					Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//					startActivity(intent);
//				}
//			});
//			alert.show();
//		}
//    }
    

}