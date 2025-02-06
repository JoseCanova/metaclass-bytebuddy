package metaclass.bytebuddy;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.nanotek.meta.model.rdbms.RdbmsMetaClassAttribute;

/**Steps to Build the Decision Tree
Define the Decision Nodes: Each decision node represents a condition based on the attributes of the RdbmsMetaClassAttribute class.

Define the Leaf Nodes: Each leaf node represents an annotation that should be applied.

Traverse the Tree: Based on the attributes of the RdbmsMetaClassAttribute, traverse the decision tree to determine the set of annotations.
**/

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import net.bytebuddy.description.annotation.AnnotationDescription;

//TODO: Prepare the decision three based on co-pilot suggestions
public class AnnotationDecisionTree {

    public List<AnnotationDescription> determineAnnotations(RdbmsMetaClassAttribute attribute) {
        List<AnnotationDescription> annotations = new ArrayList<>();

        // Check if the attribute is required
        if (attribute.isRequired()) {
            annotations.add(AnnotationDescription.Builder.ofType(NotNull.class).build());
        }

        // Check the SQL type and add relevant annotations
        if ("VARCHAR".equalsIgnoreCase(attribute.getSqlType())) {
            annotations.add(AnnotationDescription.Builder.ofType(Size.class)
                    .define("max", Integer.parseInt(attribute.getLength()))
                    .build());
            if (attribute.getColumnName().toLowerCase().contains("email")) {
                annotations.add(AnnotationDescription.Builder.ofType(Email.class).build());
            }
        }

        // Check if the attribute is part of the primary key
        if (attribute.isPartOfId()) {
            annotations.add(AnnotationDescription.Builder.ofType(NotEmpty.class).build());
        }

        // Add other custom conditions here based on the attributes

        return annotations;
    }
    
    @Test
    void testAiSuggestion() {
        RdbmsMetaClassAttribute attribute = new RdbmsMetaClassAttribute();
        attribute.setRequired(true);
        attribute.setSqlType("VARCHAR");
        attribute.setLength("255");
        attribute.setColumnName("user_email");
        attribute.setPartOfId(true);

        AnnotationDecisionTree decisionTree = new AnnotationDecisionTree();
        List<AnnotationDescription> annotations = decisionTree.determineAnnotations(attribute);

        annotations.forEach(System.out::println);
    }
}
