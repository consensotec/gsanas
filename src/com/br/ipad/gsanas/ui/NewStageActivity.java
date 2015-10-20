package com.br.ipad.gsanas.ui;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.br.ipad.gsanas.exception.FacadeException;
import com.br.ipad.gsanas.facade.Facade;
import com.br.ipad.gsanas.model.Floor;
import com.br.ipad.gsanas.model.ServiceOrder;
import com.br.ipad.gsanas.model.ServiceType;
import com.br.ipad.gsanas.model.Stage;
import com.br.ipad.gsanas.util.Constants;
import com.br.ipad.gsanas.util.Util;

public class NewStageActivity extends Activity implements OnClickListener, OnItemSelectedListener, OnFocusChangeListener {
	
	private Stage stage;
	private ServiceOrder serviceOrder;
	List<ServiceType> colectionServiceType = null;
	List<Floor> colectionFloor = null;
	private boolean edit=false;
	private EditText totalArea;
	private LinearLayout llTotal;
	private TextView titleTotalM2;
	private LinearLayout llWidth;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Facade.setContext(this);
		setContentView(R.layout.newstage);
		
		this.stage = (Stage) getIntent().getSerializableExtra("stage");
		this.serviceOrder = (ServiceOrder) getIntent().getSerializableExtra("serviceOrder");
		
		//Preenchendo combo Etapa		
		Facade facade = Facade.getInstance();		
		try {
			colectionServiceType = facade.getAllServiceTypeByIndexStageNew(1);			
		} catch (FacadeException e) {
			e.printStackTrace();
		}		
		
		Spinner stages = (Spinner) findViewById(R.id.newstagespinner);
		ArrayAdapter<ServiceType> adapterStage = new ArrayAdapter<ServiceType>(this,
		            android.R.layout.simple_spinner_item, colectionServiceType);
		adapterStage.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		stages.setAdapter(adapterStage);		
		stages.setOnItemSelectedListener(this);		
				
		//Pega as informacoes da etapa recebida como parametro e seta na tela
		if (stage!=null){
			EditText width= (EditText) findViewById(R.id.newstagewidtheditText);
			if (stage.getWidth()!=null && stage.getWidth().intValue()!=0){
				width.setText(stage.getWidth().toString());
			}
			EditText height = (EditText) findViewById(R.id.newstageheighteditText);
			if (stage.getHeight()!=null && stage.getHeight().intValue()!=0){
				height.setText(stage.getHeight().toString());
			}
			
			titleTotalM2 =  (TextView) findViewById(R.id.newstageTitleTotalAreatextView);
			
			//LinearLayout responsável pelo campo de área = 1
			llWidth = (LinearLayout) findViewById(R.id.llWidth);
			
			llTotal = (LinearLayout) findViewById(R.id.llTotal);
			totalArea = (EditText) findViewById(R.id.newstageTotalAreaeditText);
			llTotal.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if(titleTotalM2.getText().toString().equals(getString(R.string.str_stage_total_area))){
						calculateArea();
					}
					return false;
				}
			});
			if (stage.getTotalArea()!=null && stage.getTotalArea().intValue()!=0){				
				totalArea.setText(stage.getTotalArea().toString());
			} else {
				if (width!=null && width.getText()!=null && !width.getText().toString().equals("") && 
						height!=null && height.getText()!=null && !height.getText().toString().equals("")) {
					BigDecimal area = stage.getWidth().multiply(stage.getHeight());
					totalArea.setText(area.toString());
				}
			}
			
			//Selecionando combo Etapa	
			if (stage.getServiceType()!=null){
				for (int i=0; i<colectionServiceType.size();i++){
					if (colectionServiceType.get(i).getId().intValue()==stage.getServiceType().getId().intValue()){
						stages.setSelection(i);
						break;
					}
				}
			}
			if (stage.getServiceType()!=null){
				edit = true;
				preenchePavimento(stage.getServiceType());				
			}
		}
		
		//Evento dos botoes
		((Button) findViewById(R.id.stageBackbutton)).setOnClickListener(this);
		((Button) findViewById(R.id.stageInsertbutton)).setOnClickListener(this);
		if (edit){
			((Button) findViewById(R.id.stageInsertbutton)).setText(getString(R.string.str_stage_delete));
			
			//Desabilitar campos
			((Spinner) findViewById(R.id.newstagespinner)).setEnabled(false);
			((Spinner) findViewById(R.id.newstagefloorKindspinner)).setEnabled(false);
			
			if (stage.getServiceType().getIndexAccountKind().equals(Constants.YES) ){
				llWidth.setVisibility(View.VISIBLE);
			}else{
				titleTotalM2.setText(getString(R.string.str_stage_m2));	
				llWidth.setVisibility(View.GONE);
			}
			
		} else {
			((Button) findViewById(R.id.stageInsertbutton)).setText(getString(R.string.str_stage_save));
		}		
	}

	public void onClick(View arg0) {
		if (arg0.getId()==R.id.stageInsertbutton && !edit){
			//Recupera valores e inseri etapa no BD
			ServiceType st = (ServiceType) ((Spinner) findViewById(R.id.newstagespinner)).getSelectedItem();
			stage.setServiceType(st);
			Floor floor = (Floor) ((Spinner) findViewById(R.id.newstagefloorKindspinner)).getSelectedItem();
			stage.setFloor(floor);
			
			EditText width = (EditText) findViewById(R.id.newstagewidtheditText);
			if (llWidth.getVisibility() == View.VISIBLE){
				
				if (width!=null && width.getText()!=null && !width.getText().toString().equals("")){
					// Verifica se o valor digitado é válido ou diferente de 0
					if(Util.verificaSeBigDecimalQtt(width.getText().toString()) == false){
						showAlert(getString(R.string.str_stage_attention), 
								getString(R.string.str_stage_width_alert));	
						return;
					}
					BigDecimal widthFormated = new BigDecimal(width.getText().toString());
					stage.setWidth(widthFormated);
											
				} else {
					showAlert(getString(R.string.str_stage_attention), 
						getString(R.string.str_stage_required_fields)+"\n \n"+getString(R.string.str_stage_width));	
					return;
				}
			}			
			
			EditText height = (EditText) findViewById(R.id.newstageheighteditText);
			if (llWidth.getVisibility() == View.VISIBLE){
				if (height!=null && height.getText()!=null && !height.getText().toString().equals("")){
					// Verifica se o valor digitado é válido ou diferente de 0
					if(Util.verificaSeBigDecimalQtt(height.getText().toString()) == false){
						showAlert(getString(R.string.str_stage_attention), 
								getString(R.string.str_stage_height_alert));	
						return;
					}
					BigDecimal heightFormated = new BigDecimal(height.getText().toString());
					stage.setHeight(heightFormated);
				} else {
					showAlert(getString(R.string.str_stage_attention), 
						getString(R.string.str_stage_required_fields)+"\n \n"+getString(R.string.str_stage_height));
					return;
				}
			
			}
			EditText area = (EditText) findViewById(R.id.newstageTotalAreaeditText);
			titleTotalM2 = (TextView) findViewById(R.id.newstageTitleTotalAreatextView);
			if(area.isEnabled()){
				if (area!=null && area.getText()!=null && !area.getText().toString().equals("")){
					if (titleTotalM2!=null && titleTotalM2.getText()!=null && titleTotalM2.getText().toString().equals(getString(R.string.str_stage_m2))){	
						// Limita o tamanho máximo para 6
						int maxLength = 6;
						InputFilter[] fArray = new InputFilter[1];
						fArray[0] = new InputFilter.LengthFilter(maxLength);
						
						// Verifica se o valor digitado é válido ou diferente de 0
						if(Util.verificaSeBigDecimalQtt(area.getText().toString()) == false){
							showAlert(getString(R.string.str_stage_attention), 
									getString(R.string.str_stage_m3_alert));	
							return;
						}
						BigDecimal areaFormated = new BigDecimal(area.getText().toString());
						stage.setTotalArea(areaFormated);
					}
				} else {
					showAlert(getString(R.string.str_stage_attention), 
							getString(R.string.str_stage_required_fields)+"\n \n"+titleTotalM2.getText().toString());	
						return;
					}
			}
			
			try {				
				stage.setServiceOrder(serviceOrder);
				Facade.getInstance().insertStage(stage);
				finish();
			} catch (FacadeException e) {
				e.printStackTrace();
			}
			
		} else if (arg0.getId()==R.id.stageInsertbutton && edit) {
			//DELETAR - Msg de confirmacao 
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle(getString(R.string.str_stage_confirm));
			alert.setMessage(getString(R.string.str_stage_are_you_right)+" "+stage.getServiceType().getDescriptionServiceType());
			alert.setPositiveButton(getString(R.string.str_stage_yes), new DialogInterface.OnClickListener() {  
			      public void onClick(DialogInterface dialog, int which) {  
			    	try {
			    		finish();
						Facade.getInstance().removeStage(stage);						
					} catch (FacadeException e) {
						e.printStackTrace();
					}
			      }
			}); 
			alert.setNegativeButton(getString(R.string.str_stage_not), new DialogInterface.OnClickListener(){
				 public void onClick(DialogInterface dialog, int which) {
					 return;
				 }
			});
			alert.show();
		} else {
			finish();
		}
	}

	private boolean validaRepeticaoServiceType(ServiceType serviceType) {
		try {
			ArrayList<Stage> listStage = Facade.getInstance().getAllStagesByServiceOrderId(serviceOrder.getId());
			for (Stage s : listStage){
				if (s.getServiceType().getId().intValue()==serviceType.getId().intValue()){
					return false;
				}
			}
			
		} catch (FacadeException e) {
			e.printStackTrace();
		}
		
		return true;
	}

	public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {		
		//Obtem servicoTipo selecionada
		ServiceType serviceType = colectionServiceType.get(position);
		if (serviceType.getIndexSidewalkFloor().intValue()==1 || serviceType.getIndexStreetFloor().intValue()==1){
			//Deixa combo Pavimento visivel
			((TextView) findViewById(R.id.floorKindtextView)).setVisibility(0);
			((Spinner) findViewById(R.id.newstagefloorKindspinner)).setVisibility(0);
			((ImageView) findViewById(R.id.image2)).setVisibility(0);
			
			//Prenche combo
			if (colectionFloor==null){
				preenchePavimento(serviceType);
			}
		}
		EditText width = (EditText) findViewById(R.id.newstagewidtheditText);
		EditText height = (EditText) findViewById(R.id.newstageheighteditText);
		
		if (!edit){
			if (serviceType.getIndexAccountKind().intValue()==1){
				//Habilitar campos

				llWidth.setVisibility(View.VISIBLE);
				width.setEnabled(true);	
				width.setOnFocusChangeListener(this);
				
				height.setEnabled(true);
				height.setOnFocusChangeListener(this);
				
				titleTotalM2.setText(getString(R.string.str_stage_total_area));
				((EditText) findViewById(R.id.newstageTotalAreaeditText)).setEnabled(false);			
			} else {

				llWidth.setVisibility(View.GONE);
				//Desabilitar campos
							
				//Deixa combo Pavimento invisivel
				((Spinner) findViewById(R.id.newstagefloorKindspinner)).setVisibility(4);
							
				titleTotalM2.setText(getString(R.string.str_stage_m2));	
				((EditText) findViewById(R.id.newstageTotalAreaeditText)).setEnabled(true);
			}
			((EditText) findViewById(R.id.newstageTotalAreaeditText)).setText("");
			width.setText("");
			height.setText("");
		}
	}

	private void preenchePavimento(ServiceType serviceType) {
		try {
			if (serviceType.getIndexSidewalkFloor().intValue()==1){
				colectionFloor = Facade.getInstance().getAllFloorsByFloorKind(1);
			} else {
				colectionFloor = Facade.getInstance().getAllFloorsByFloorKind(2);
			}
			
			Spinner floors = (Spinner) findViewById(R.id.newstagefloorKindspinner);
			ArrayAdapter<Floor> adapterStage = new ArrayAdapter<Floor>(this,
			            android.R.layout.simple_spinner_item, colectionFloor);
			adapterStage.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			floors.setAdapter(adapterStage);		
			
			if(edit && stage.getFloor()!=null){
				//Selecionando combo Pavimento	
				for (int i=0; i<colectionFloor.size();i++){
					if (colectionFloor.get(i).getId().intValue()==stage.getFloor().getId().intValue()){
						floors.setSelection(i);
						break;
					}
				}
			}
		} catch (FacadeException e) {
			e.printStackTrace();
		}
	}

	public void onNothingSelected(AdapterView<?> arg0) { }

		
	public void onFocusChange(View v, boolean hasFocus) {
		calculateArea();
		
	}

	private void calculateArea() {
		//Calcula área
		EditText width = (EditText) findViewById(R.id.newstagewidtheditText);
		EditText height = (EditText) findViewById(R.id.newstageheighteditText);
		if (width.getText()!=null && !width.getText().toString().equals("") && 
				height.getText()!=null && !height.getText().toString().equals("")) {
			Double areaTotal = Double.valueOf(width.getText().toString()) * Double.valueOf(height.getText().toString());
			BigDecimal area = new BigDecimal(areaTotal);
			((EditText) findViewById(R.id.newstageTotalAreaeditText)).setText(area.toString());
		}
	}

	private void showAlert(String title, String message){
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle(title);
		alert.setMessage(message);
		alert.setPositiveButton(getString(R.string.str_stage_ok) 
			, new DialogInterface.OnClickListener() {  
		      public void onClick(DialogInterface dialog, int which) {  
		        return;  
		    } }); 
		alert.show();
	}

}