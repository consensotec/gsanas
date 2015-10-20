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

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.br.ipad.gsanas.exception.FacadeException;
import com.br.ipad.gsanas.facade.Facade;
import com.br.ipad.gsanas.model.HidrometerSubstitution;
import com.br.ipad.gsanas.model.ServiceOrder;
import com.br.ipad.gsanas.util.Constants;

public class PhotoTypeActivity extends Activity {
			
	private LinearLayout llBegin;
	private LinearLayout llDuring;
	private LinearLayout llEnd;
	Uri outputFileUri;
	
	private ServiceOrder serviceOrder;
	
	private HidrometerSubstitution hidrometerSubstitution;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_type);
        Facade.setContext(this);
		
		serviceOrder = (ServiceOrder) getIntent().getSerializableExtra("serviceOrder");
		try {
			hidrometerSubstitution = Facade.getInstance().getHidrometerSubstitutionById(serviceOrder.getId());
		} catch (FacadeException e) {
			Log.e( Constants.CATEGORY , e.getMessage());
			e.printStackTrace();
		}
		if(hidrometerSubstitution != null){
			if(hidrometerSubstitution.getDescriptionPhoto() != null){
				LinearLayout llHidrometer = (LinearLayout) findViewById(R.id.btHidrometer);
				
				ImageView imgSubstitution = new ImageView(PhotoTypeActivity.this);
				imgSubstitution.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));		
				imgSubstitution.setImageResource(R.drawable.replace);
				
				LinearLayout.LayoutParams margin = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				margin.setMargins(1, 15, 20, 1);
				llHidrometer.addView(imgSubstitution,margin);
				
				TextView textSubstitution = new TextView(PhotoTypeActivity.this);
				textSubstitution.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));		
				textSubstitution.setText(R.string.str_photo_type_hidrometer);
				textSubstitution.setTextColor(Color.WHITE);
				textSubstitution.setPadding(0, 25,0, 0);
			    textSubstitution.setTypeface(null,Typeface.BOLD);
			    llHidrometer.addView(textSubstitution);
				
			    ImageView imgLine = new ImageView(PhotoTypeActivity.this);
			    imgLine.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));		
			    imgLine.setImageResource(R.drawable.line);
			    
			    LinearLayout mainLinearLayout = (LinearLayout) findViewById(R.id.mainLl);
			    
			    LinearLayout.LayoutParams marginLine = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			    marginLine.setMargins(1, 15, 0, 1);
				mainLinearLayout.addView(imgLine,margin);
				
				llHidrometer.setOnClickListener(new OnClickListener() {
					
					public void onClick(View arg0) {
						
						Intent i = new Intent(PhotoTypeActivity.this,Gallery.class);
						i.putExtra("photoType", Constants.HIDROMETER);
						i.putExtra("serviceOrder", serviceOrder);
						startActivity(i);
						
					}
				});
							
			}
					
		}
		
		llBegin = (LinearLayout) findViewById(R.id.btBegin);
		llDuring = (LinearLayout) findViewById(R.id.btDuring);
		llEnd = (LinearLayout) findViewById(R.id.btEnd);
		
		llBegin.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				Intent i = new Intent(PhotoTypeActivity.this,Gallery.class);
				i.putExtra("photoType", Constants.BEGINNING);
				i.putExtra("serviceOrder", serviceOrder);
				startActivity(i);

			}
		});
		
		llDuring.setOnClickListener(new OnClickListener() {
				
			public void onClick(View v) {
				
				Intent i = new Intent(PhotoTypeActivity.this,Gallery.class);
				i.putExtra("photoType", Constants.DURING);
				i.putExtra("serviceOrder", serviceOrder);
				startActivity(i);

			}
		});

		llEnd.setOnClickListener(new OnClickListener() {
		
			public void onClick(View v) {
				
				Intent i = new Intent(PhotoTypeActivity.this,Gallery.class);
				i.putExtra("photoType", Constants.END);
				i.putExtra("serviceOrder", serviceOrder);
				startActivity(i);

			}
		});		
		
	}
	
	 	@Override
	    public void onBackPressed() {
	    	Intent i = new Intent(PhotoTypeActivity.this,ServiceOrderActvity.class);
			i.putExtra("editar", serviceOrder);	
			startActivity(i);
	       return;
	    }
			
		
}
	

