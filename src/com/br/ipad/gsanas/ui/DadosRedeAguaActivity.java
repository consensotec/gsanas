package com.br.ipad.gsanas.ui;

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



import java.math.BigDecimal;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.br.ipad.gsanas.adapters.ElementoReferenciaAdpter;
import com.br.ipad.gsanas.adapters.LigacaoAguaMaterialAdpter;
import com.br.ipad.gsanas.exception.BusinessException;
import com.br.ipad.gsanas.exception.FacadeException;
import com.br.ipad.gsanas.facade.Facade;
import com.br.ipad.gsanas.model.ElementoReferenciaHelper;
import com.br.ipad.gsanas.model.LigacaoAguaMaterial;
import com.br.ipad.gsanas.model.LigacaoAguaMaterial.LigacaoAguaMateriais;
import com.br.ipad.gsanas.model.ServiceOrder;
import com.br.ipad.gsanas.util.Constants;
import com.br.ipad.gsanas.util.Util;

public class DadosRedeAguaActivity extends Activity {

	private ArrayList<LigacaoAguaMaterial> ligacaoAguaMaterialLista;
	private LigacaoAguaMaterialAdpter ligacaoAguaMaterialAdapter;
	private Spinner spinnerLigacaoAguaMaterial;
	
	private ArrayList<ElementoReferenciaHelper> elementoReferenciaListaHelper;
	private ElementoReferenciaAdpter elementoReferenciaAdapter;
	private Spinner spinnerElementoReferencia;
	
	private EditText profundidade;
	private EditText diametro;
	private EditText distancia;
//	private EditText elementoReferencia;
	
	private Intent i;
	private ServiceOrder serviceOrder;
	private Button btMenu;
	private Button btConfirmarDadosRedeAgua;

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		Facade.setContext(this);
		setContentView(R.layout.dados_rede_agua);
		
		i = getIntent();
		serviceOrder = (ServiceOrder) i.getSerializableExtra("serviceOrder");

		setUpWidgets();
		botaoMenu();
		
		
	}
	
	private void setUpWidgets() {
		
		clickBotaoConfimarDadosRedeAgua();
		
		
		profundidade = (EditText) findViewById(R.id.profundidade);
		if(serviceOrder.getProfundidade()!=null){
			profundidade.setText(serviceOrder.getProfundidade().toString());
		}
		ocultarTecladoVitual(profundidade);

		diametro = (EditText) findViewById(R.id.diametro);
		if(serviceOrder.getDiametro()!=null){
			diametro.setText(serviceOrder.getDiametro().toString());
		}
		ocultarTecladoVitual(diametro);
		
		distancia = (EditText) findViewById(R.id.distancia); 
		if(serviceOrder.getDistanciaRedeLinhaAgua()!=null){
			distancia.setText(serviceOrder.getDistanciaRedeLinhaAgua().toString());
		}
		ocultarTecladoVitual(distancia);
		
		carregarTipoMaterial();
		carregarElementoReferencia();
	}

	public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_MENU)
        {
        	Intent it = new Intent(this,MenuActivity.class);
    		it.putExtra("serviceOrder", serviceOrder);
    		startActivity(it);
    		return true;
        }

        return true;
    }
	
	private void carregarTipoMaterial(){
		
		spinnerLigacaoAguaMaterial = (Spinner) findViewById(R.id.spinnerTipoMaterial);
		
		try {
			ligacaoAguaMaterialLista = Facade.getInstance().pesquisarOrderBy(new LigacaoAguaMaterial(), LigacaoAguaMateriais.DESCRICAO);
			
		} catch (FacadeException e) {
			e.printStackTrace();
		}
		
		if(ligacaoAguaMaterialLista!=null){
			ligacaoAguaMaterialAdapter = new LigacaoAguaMaterialAdpter(DadosRedeAguaActivity.this, ligacaoAguaMaterialLista );
			spinnerLigacaoAguaMaterial.setAdapter(ligacaoAguaMaterialAdapter);
		
			Integer posicaoSpinner = 0;
			try {
				if(serviceOrder.getLigacaoAguaMaterial()!=null){
					posicaoSpinner = ligacaoAguaMaterialAdapter.getPosition
							(Facade.getInstance().pesquisarPorId(serviceOrder.getLigacaoAguaMaterial().getId(), new LigacaoAguaMaterial()));
				}
			} catch (FacadeException e) {
				e.printStackTrace();
			}
			
			spinnerLigacaoAguaMaterial.setSelection(posicaoSpinner);
		
			spinnerLigacaoAguaMaterial.setOnItemSelectedListener(new OnItemSelectedListener() {

			
				public void onItemSelected(AdapterView<?> arg0, View view, int position,
	 					long id) {
					
				}
				
				public void onNothingSelected(AdapterView<?> arg0) {
					
				}
			});
		
		}
	}
	
	private void carregarElementoReferencia(){
		
		spinnerElementoReferencia = (Spinner) findViewById(R.id.spinnerElementoReferencia);
		
		elementoReferenciaListaHelper = new ArrayList<ElementoReferenciaHelper>();
		
		ElementoReferenciaHelper helper = new ElementoReferenciaHelper(1, "CALCADA");
		elementoReferenciaListaHelper.add(helper);
		
		helper = new ElementoReferenciaHelper(2, "RUA");
		elementoReferenciaListaHelper.add(helper);
		
		elementoReferenciaAdapter = new ElementoReferenciaAdpter(DadosRedeAguaActivity.this, elementoReferenciaListaHelper );
		spinnerElementoReferencia.setAdapter(elementoReferenciaAdapter);
		
		Integer posicaoSpinner = 0;
		if(serviceOrder.getDescricaoElementoReferencia()!=null){
			if(serviceOrder.getDescricaoElementoReferencia().equalsIgnoreCase("CALCADA")){
				posicaoSpinner = 1;
			}else{
				posicaoSpinner = 2;
			}
		}
				
		spinnerElementoReferencia.setSelection(posicaoSpinner);
	
		spinnerElementoReferencia.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View view, int position,
 					long id) {
				
			}
			
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
		
	}

	private void clickBotaoConfimarDadosRedeAgua(){
		
		btConfirmarDadosRedeAgua = (Button) findViewById(R.id.btDadosRedeAgua);
		
		btConfirmarDadosRedeAgua.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {					
		
				LigacaoAguaMaterial ligacaoAguaMaterial = 
						(LigacaoAguaMaterial) spinnerLigacaoAguaMaterial.getSelectedItem();
				
			
					serviceOrder.setDiametro(Util.convertStringToBigDecimal(diametro.getText().toString()));
					serviceOrder.setProfundidade(Util.convertStringToBigDecimal(profundidade.getText().toString()));
					serviceOrder.setDistanciaRedeLinhaAgua(Util.convertStringToBigDecimal(distancia.getText().toString()));
					serviceOrder.setLigacaoAguaMaterial(ligacaoAguaMaterial);
					
					ElementoReferenciaHelper elementoReferenciaHelper = 
							(ElementoReferenciaHelper) spinnerElementoReferencia.getSelectedItem();
					
					if(elementoReferenciaHelper.getId() !=null && !elementoReferenciaHelper.getId().equals(0)){
						serviceOrder.setDescricaoElementoReferencia(elementoReferenciaHelper.getDescricao());
					}
					
					
					try {
						Facade.getInstance().updateServiceOrder(serviceOrder);
					}catch (BusinessException e) {
						e.printStackTrace();
					} catch (FacadeException e) {
						e.printStackTrace();
					}
					
					AlertDialog.Builder alert = new AlertDialog.Builder(DadosRedeAguaActivity.this);
					alert.setMessage(getString(R.string.str_dadosRedeAgua_sucesso));
					alert.setNeutralButton(Constants.ALERT_OK, new DialogInterface.OnClickListener() {
						
						public void onClick(DialogInterface arg0, int arg1) {
							
						}
					});
					alert.show();
				
			}
		});
	}
	
	//Ocultar teclado virtual ao apertar o botao enter.
	private void ocultarTecladoVitual (final EditText editText){
		editText.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (event != null&& (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
					InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

					in.hideSoftInputFromWindow(editText.getApplicationWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);

					return true;

				}
				return false;
			}
		});
	}
	
	private void botaoMenu(){
		btMenu = (Button) findViewById(R.id.btMenu);
		btMenu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onKeyUp(KeyEvent.KEYCODE_MENU, null);
			}
		});
	}

	
}
