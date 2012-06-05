/*
Copyright 2012 James Iry

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package pogofish.jadt.checker;

/**
 * SemanticException indicating that a data type and a constructor had the same name
 *
 * @author jiry
 */
public class DuplicateDataTypeException extends SemanticException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 7966096437224245037L;
	private String dataTypeName;
    
    public DuplicateDataTypeException(String dataTypeName) {
        super("Cannot have two datatypes named " + dataTypeName);
        this.dataTypeName = dataTypeName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + dataTypeName.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        DuplicateDataTypeException other = (DuplicateDataTypeException)obj;
        if (!dataTypeName.equals(other.dataTypeName)) return false;
        return true;
    }

}