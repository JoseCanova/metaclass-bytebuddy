package org.nanotek.metaclass.bytebuddy;

import org.nanotek.meta.model.rdbms.RdbmsMetaClass;

public class ConfigurableRdbmsEntityBase extends RdbmsEntityBaseBuddy {

	private boolean annotatedValidation = false;
	
	private boolean annotateJpa = false;
	
	private boolean annotateSpring = false;
	
	
	public ConfigurableRdbmsEntityBase(RdbmsMetaClass metaClass) {
		super(metaClass);
	}
	
	public static ConfigurableRdbmsEntityBase instance(RdbmsMetaClass metaClass) {
		return new ConfigurableRdbmsEntityBase(metaClass);
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
