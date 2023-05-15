class EntityValidationResult {

    _fieldErrors;

    get fieldErrors() {
        return this._fieldErrors;
    }

    set fieldErrors(value) {
        this._fieldErrors = value;
    }
}

export class FieldError {

    _fieldName;
    _errorCode;
    _errorMessage;

    constructor(fieldName, errorCode, errorMessage) {
        this._fieldName = fieldName;
        this._errorCode = errorCode;
        this._errorMessage = errorMessage;
    }

    get fieldName() {
        return this._fieldName;
    }

    set fieldName(value) {
        this._fieldName = value;
    }

    get errorCode() {
        return this._errorCode;
    }

    set errorCode(value) {
        this._errorCode = value;
    }

    get errorMessage() {
        return this._errorMessage;
    }

    set errorMessage(value) {
        this._errorMessage = value;
    }
}

export default EntityValidationResult;