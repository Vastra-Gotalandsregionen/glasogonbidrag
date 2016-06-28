package se.vgregion.glasogonbidrag.library;

import se.vgregion.glasogonbidrag.model.ImportError;
import se.vgregion.glasogonbidrag.model.ValidationError;
import se.vgregion.glasogonbidrag.parser.ParseOutputData;

import java.util.List;
import java.util.Map;

/**
 * Created by martlin on 2016/06/27.
 */
public class StorableRepositoryImpl implements StorableRepository {
    private List<ImportError> importErrors;
    private List<ValidationError> validationErrors;

    public void catalog(Map<String, ParseOutputData> data) {
        for (String key : data.keySet()) {
            catalog(data.get(key));
        }
    }

    private void catalog(ParseOutputData data) {
        importErrors.addAll(data.getImportErrors());
        validationErrors.addAll(data.getValidationErrors());
    }
}
