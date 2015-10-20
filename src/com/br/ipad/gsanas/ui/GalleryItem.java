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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.br.ipad.gsanas.exception.FacadeException;
import com.br.ipad.gsanas.facade.Facade;
import com.br.ipad.gsanas.model.Photo;
import com.br.ipad.gsanas.model.ServiceOrder;
import com.br.ipad.gsanas.util.Constants;
import com.br.ipad.gsanas.util.Util;

public class GalleryItem extends Activity {
			
	private String directory;
	
	private File photoFile;
	private ServiceOrder serviceOrder;
	private int photoType;
	ImageView myImageView;
	private Button btMenu;
	
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Facade.setContext(this);
		setContentView(R.layout.gallery_item);
		
		serviceOrder = (ServiceOrder) getIntent().getSerializableExtra("serviceOrder");
		photoType = getIntent().getIntExtra("photoType",0); 
		
		directory = getIntent().getStringExtra("photoPath");
		photoFile = new File(directory);
		
		Bitmap bt = Util.decodeFile(photoFile);
		myImageView = (ImageView) findViewById(R.id.thumbImage);
		//uso de menos memoria
	    myImageView.setDrawingCacheEnabled(false);
	    	    
	    myImageView.setImageBitmap(bt);
	    
	    btMenu = (Button) findViewById(R.id.btMenu);
		btMenu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				openOptionsMenu();
			}
		});
	}
			
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	menu.add(0,Constants.DELETE,0,R.string.str_gallery_item_delete);
    	return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
    	
    	switch (item.getItemId()){
    	case Constants.DELETE:
    		
    		//Remove foto do diretório
    		photoFile.delete();
    		
    		Photo photo = null;
    		
    		//Pega foto do BD
			try {
				photo = Facade.getInstance().getPhotoByPath(serviceOrder.getId(), directory);
			} catch (FacadeException e) {
				e.printStackTrace();
				Log.e( Constants.CATEGORY , e.getMessage());
			}    	
			
			//Remove foto do BD
			if(photo != null){
	    		try {
					Facade.getInstance().removePhoto(photo);
				} catch (FacadeException e) {
					e.printStackTrace();
					Log.e( Constants.CATEGORY , e.getMessage());
				}
			}else{
				 AlertDialog.Builder alert = new AlertDialog.Builder(GalleryItem.this);
				  alert.setMessage(getString(R.string.str_gallery_item_error));
				  alert.setPositiveButton(Constants.ALERT_OK, new DialogInterface.OnClickListener() {
						
						public void onClick(DialogInterface arg0, int arg1) {
							 
						}
				  });
				  alert.show();
			}
    		
    		Intent i = new Intent(GalleryItem.this,Gallery.class);
    		i.putExtra("serviceOrder", serviceOrder);
    		i.putExtra("photoType", photoType);
    		startActivity(i);
    		
    		return true;
    		
    	}
    	return super.onMenuItemSelected(featureId, item); 
    }
    
    @Override
    protected void onPause() {
    	// TODO Auto-generated method stub
    	super.onPause();
    	Intent i = new Intent(GalleryItem.this,Gallery.class);
		i.putExtra("serviceOrder", serviceOrder);
		i.putExtra("photoType", photoType);
		startActivity(i);
    }
    
   

}
