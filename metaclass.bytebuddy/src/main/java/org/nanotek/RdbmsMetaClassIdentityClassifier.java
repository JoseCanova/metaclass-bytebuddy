package org.nanotek;

import java.util.Optional;

import org.nanotek.meta.model.rdbms.RdbmsMetaClass;

public interface RdbmsMetaClassIdentityClassifier {

	public static RdbmsMetaClassIdentityClassifier on() {
		return new RdbmsMetaClassIdentityClassifier() {};
	}
	
	default Optional<MetaClassIdentityClassification> 
			classifyIdentity(RdbmsMetaClass mc) {
		return mc.getIdentity()
					.getColumns()
					.stream()
					.count() > 1? 
						MetaClassClassificationBuilder.on().withMetaClassClassification(KeyClassification.COMPOSITE).build()
								:MetaClassClassificationBuilder.on().withMetaClassClassification(KeyClassification.SIMPLE).build();
	}
	
	public class MetaClassClassificationBuilder {
		    
			static MetaClassClassificationBuilder on() {
				return new MetaClassClassificationBuilder();
			}
		
			private MetaClassIdentityClassification metaClassClassification;

			MetaClassClassificationBuilder withMetaClassClassification(KeyClassification kc) {
				this.metaClassClassification  = new MetaClassIdentityClassification(kc);
				return this;
			}
			
			Optional<MetaClassIdentityClassification> build(){
				return Optional.ofNullable(metaClassClassification);
			}
	}
	
	public static record MetaClassIdentityClassification(KeyClassification classification) {
	}
	
	public enum KeyClassification{
		SIMPLE,
		COMPOSITE
	}
}
