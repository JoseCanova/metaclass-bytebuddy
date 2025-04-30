package org.nanotek.metaclass.bytebuddy;

import org.nanotek.meta.model.rdbms.RdbmsMetaClass;

public class ConfigurableRdbmsEntityBase  {

	private boolean annotatedValidation = false;
	
	private boolean annotateJpa = false;
	
	private boolean annotateSpring = false;
	
	
	public ConfigurableRdbmsEntityBase(RdbmsMetaClass metaClass) {
		super();
	}
	
	public boolean isAnnotatedValidation() {
		return annotatedValidation;
	}

	public void setAnnotatedValidation(boolean annotatedValidation) {
		this.annotatedValidation = annotatedValidation;
	}

	public boolean isAnnotateJpa() {
		return annotateJpa;
	}

	public void setAnnotateJpa(boolean annotateJpa) {
		this.annotateJpa = annotateJpa;
	}

	public boolean isAnnotateSpring() {
		return annotateSpring;
	}

	public void setAnnotateSpring(boolean annotateSpring) {
		this.annotateSpring = annotateSpring;
	}
	
	

}
