package org.nanotek;

import java.util.Optional;

import org.nanotek.meta.model.rdbms.RdbmsMetaClass;

public interface CompositeKeyClassifier {

	default Optional<MetaClassClassification> 
			verifyMultipleAttributeKeyTable(RdbmsMetaClass mc) {
		return mc.getIdentity().getColumns().stream().count() > 1? 
						MetaClassClassificationBuilder.on().withMetaClassClassification(KeyClassification.COMPOSITE).build()
								:MetaClassClassificationBuilder.on().withMetaClassClassification(KeyClassification.SIMPLE).build();
	}
	
	public class MetaClassClassificationBuilder {
		    
			static MetaClassClassificationBuilder on() {
				return new MetaClassClassificationBuilder();
			}
		
			private MetaClassClassification metaClassClassification;

			MetaClassClassificationBuilder withMetaClassClassification(KeyClassification kc) {
				this.metaClassClassification  = new MetaClassClassification(kc);
				return this;
			}
			
			Optional<MetaClassClassification> build(){
				return Optional.ofNullable(metaClassClassification);
			}
	}
	
	public static record MetaClassClassification(KeyClassification classification) {
	}
	
	enum KeyClassification{
		SIMPLE,
		COMPOSITE
	}
}
