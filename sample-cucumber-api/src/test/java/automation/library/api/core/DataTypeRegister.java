package automation.library.api.core;

import io.cucumber.core.api.TypeRegistry;
import io.cucumber.core.api.TypeRegistryConfigurer;
import io.cucumber.datatable.DataTableType;
import io.cucumber.datatable.TableEntryTransformer;

import java.util.Locale;
import java.util.Map;

public class DataTypeRegister implements TypeRegistryConfigurer {

    @Override
    public void configureTypeRegistry(TypeRegistry registry) {

        registry.defineDataTableType(new DataTableType(ResponseValidator.class, (TableEntryTransformer<ResponseValidator>) row -> {
            String element = row.get("element");
            String matcher = row.get("matcher");
            String value = row.get("value");
            String type = row.get("type");
            return new ResponseValidator(element, matcher, value, type);
        }));
    }

    @Override
    public Locale locale() {
        return Locale.ENGLISH;
    }

}
