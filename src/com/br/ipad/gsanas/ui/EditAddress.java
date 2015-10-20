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
import android.os.Bundle;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.br.ipad.gsanas.exception.BusinessException;
import com.br.ipad.gsanas.exception.FacadeException;
import com.br.ipad.gsanas.facade.Facade;
import com.br.ipad.gsanas.model.ServiceOrder;
import com.br.ipad.gsanas.util.Constants;
import com.br.ipad.gsanas.util.Util;

public class EditAddress extends Activity {
	private ServiceOrder serviceOrder;
	private TextView address;
	private EditText number;
	private EditText refPoint;
	private Button btEdit;
	private Button btCancel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		Facade.setContext(this);
		setContentView(R.layout.edit_address);
		
		btEdit = (Button) findViewById(R.id.btEdit);
		btCancel = (Button) findViewById(R.id.btCancel);
		address  = (TextView) findViewById(R.id.address);
		number = (EditText) findViewById(R.id.numberText);		
		refPoint  = (EditText) findViewById(R.id.refPointText);
		
		serviceOrder = (ServiceOrder) getIntent().getSerializableExtra("editAddress");
		if(serviceOrder!=null){			
			address.setText(Util.verificarNuloString(serviceOrder.getDescriptionAddres()));	
			number.setText(Util.verificarNuloString(serviceOrder.getPropertyNumber()+""));
			refPoint.setText(Util.verificarNuloString(serviceOrder.getReferencePointDescription()));
		}
		// validate number field - numbers only
		number.setKeyListener(DigitsKeyListener.getInstance());
		
				
		btEdit.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				
				serviceOrder.setPropertyNumber(number.getText().toString());
				serviceOrder.setReferencePointDescription(refPoint.getText().toString());
				try {
					Facade.getInstance().updateServiceOrder(serviceOrder);
				} catch (FacadeException e) {
					e.printStackTrace();
					Log.e( Constants.CATEGORY , e.getMessage());
				} catch (BusinessException e) {
					e.printStackTrace();
					Log.e( Constants.CATEGORY , e.getMessage());
				}
				Intent it = new Intent(EditAddress.this,ServiceOrderActvity.class);
				it.putExtra("editar", serviceOrder);
				startActivity(it);
			
			}
		});
		
		btCancel.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				Intent i = new Intent(EditAddress.this, ServiceOrderActvity.class);
				i.putExtra("editar", serviceOrder);
				startActivity(i);				
			}
		});
		
		// Desativa a tecla enter
		refPoint.setOnKeyListener(new OnKeyListener() {
			
			public boolean onKey(View arg0, int keyCode, KeyEvent arg2) {
				if ((arg2.getAction() == KeyEvent.ACTION_DOWN) &&
			            (keyCode == KeyEvent.KEYCODE_ENTER)) {
			          return true;
			        }
				return false;
			}
		});
		
	}

	
	

}
