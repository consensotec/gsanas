package com.br.ipad.gsanas.repository;

import java.util.ArrayList;

import com.br.ipad.gsanas.exception.RepositoryException;
import com.br.ipad.gsanas.model.Floor;

public interface IFloorRepository {
	
	public Floor getFloorById( int id ) throws RepositoryException;	
	public ArrayList<Floor> getAllFloors() throws RepositoryException;	
	public void insertFloor(Floor floor) throws RepositoryException;	
	public void updateFloor(Floor floor) throws RepositoryException;	
	public void removeFloor(Floor floor) throws RepositoryException;
	public ArrayList<Floor> getAllFloorsByFloorKind(int floorKind) throws RepositoryException;
}
