import withHeaderAndFooter from "../hoc/withHeaderAndFooter";
import requiresUser from "../hoc/requiresUser";
import "../css/CreateItemPage.css";
import React, {useEffect, useRef, useState} from "react";
import Api from "../Api";
import InvalidEntityException from "../exception/InvalidEntityException";

function CreateItemPage() {

    const [categoriesTrees, setCategoriesTrees] = useState([]);

    /*
        [
            [rootCategoryId1, subCategoryId, subSubCategoryId, ...],
            ...
        ]
     */
    const [selectedCategoriesSequences, setSelectedCategoriesSequences] = useState([]);

    useEffect(() => {
        Api.getAllCategories()
            .then(categories => setCategoriesTrees(categories))
            .catch(() => alert("Ошибка при получении списка категорий, свяжитесь с разработчиком"));


    }, []);

    const handleAddRootCategorySelect = (e) => {
        e.preventDefault();

        for (let i = 0; i < categoriesTrees.length; i++) {

            let currentRootCategory = categoriesTrees[i];
            let categoryIsChosen = false;

            for (let j = 0; j < selectedCategoriesSequences.length; j++) {
                if(selectedCategoriesSequences[j][0] === currentRootCategory["id"]) {
                    categoryIsChosen = true;
                }
            }

            if(!categoryIsChosen) {
                const updatedList = structuredClone(selectedCategoriesSequences);
                updatedList.push([currentRootCategory["id"]]);
                setSelectedCategoriesSequences(updatedList);
            }
        }
    }

    const handleAddSubCategorySelect = (e, parentId) => {

        e.preventDefault();

        const children = getCategoryById(parentId)["subCategories"];

        let categoryIdToAdd = -1;
        for (let i = 0; i < children.length; i++) {
            if(!getCategoryIsSelectedById(children[i]["id"])) {
                categoryIdToAdd = children[i]["id"];
            }
        }

        if(categoryIdToAdd === -1) {
            return;
        }

        const updatedSelectedCategoriesSequences = [];
        for (let i = 0; i < selectedCategoriesSequences.length; i++) {

            const currentSequence = selectedCategoriesSequences[i];

            if(currentSequence.indexOf(parentId) === -1) {
                updatedSelectedCategoriesSequences.push(currentSequence);
            } else {

                const updatedSequence = structuredClone(currentSequence);
                updatedSequence.push(categoryIdToAdd);
                updatedSelectedCategoriesSequences.push(updatedSequence);
            }
        }

        setSelectedCategoriesSequences(updatedSelectedCategoriesSequences);
    }

    const handleReduceCategoriesSequenceLength = (e, rootCategoryId) => {
        e.preventDefault();

        let updatedSelectedCategoriesSequences = [];

        for (let i = 0; i < selectedCategoriesSequences.length; i++) {

            const currentSequence = selectedCategoriesSequences[i];

            if(currentSequence[0] !== rootCategoryId) {
                updatedSelectedCategoriesSequences.push(currentSequence);
            } else if(currentSequence.length > 1) {
                let updatedSequence = structuredClone(currentSequence);
                updatedSequence = updatedSequence.slice(0, updatedSequence.length - 1);
                updatedSelectedCategoriesSequences.push(updatedSequence);
            }
        }

        setSelectedCategoriesSequences(updatedSelectedCategoriesSequences);
    }

    const handleChangeCategorySelect = (e) => {
        e.preventDefault();

        const chosenSelectIndex = e.target.selectedIndex;
        const chosenCategoryId = Number(e.target.options[chosenSelectIndex].value);

        if(getCategoryIsSelectedById(chosenCategoryId)) {
            alert("Ошибка: категория " + getCategoryById(chosenCategoryId)["name"] + " уже выбрана!");
            return;
        }

        const currentlySelectedCategoryId = Number(e.target.options[0].value);

        const updatedSelectedCategoriesSequences = [];

        for (let i = 0; i < selectedCategoriesSequences.length; i++) {

            const currentSequence = selectedCategoriesSequences[i];

            if(currentSequence.indexOf(currentlySelectedCategoryId) === -1) {
                updatedSelectedCategoriesSequences.push(currentSequence);
            } else {

                const updatedSequence = [];

                for (let j = 0; j < currentSequence[j]; j++) {
                    if(currentSequence[j] !== currentlySelectedCategoryId) {
                        updatedSequence.push(currentSequence[j]);
                    } else {
                        updatedSequence.push(chosenCategoryId);
                        break;
                    }
                }

                updatedSelectedCategoriesSequences.push(updatedSequence);
            }
        }

        setSelectedCategoriesSequences(updatedSelectedCategoriesSequences);
    }

    const getCategoryById = (id, source=categoriesTrees) => {

        for (let i = 0; i < source.length; i++) {

            if(source[i]["id"] === id) {
                return source[i];
            }

            let resultFromChildren = getCategoryById(id, source[i]["subCategories"]);
            if(resultFromChildren !== null) {
                return resultFromChildren;
            }
        }

        return null;
    }

    const getUnselectedCategorySiblingsById = (id) => {

        const category = getCategoryById(id);

        return category["parentId"] == null ?
            filterOutSelectedCategories(categoriesTrees) :
            filterOutSelectedCategories(getCategoryById(category["parentId"])["subCategories"]);
    }

    const filterOutSelectedCategories = (list) => {

        const filteredList = [];

        for (let i = 0; i < list.length; i++) {
            if(!getCategoryIsSelectedById(list[i]["id"])) filteredList.push(list[i]);
        }

        return filteredList;
    }

    const getCategoryIsSelectedById = (id) => {

        for (let i = 0; i < selectedCategoriesSequences.length; i++) {
            for (let j = 0; j < selectedCategoriesSequences[i].length; j++) {
                if(selectedCategoriesSequences[i][j] === id) return true;
            }
        }

        return false;
    }

    const getHasSubCategoriesById = (id) => {
        return getCategoryById(id)["subCategories"].length !== 0;
    }

    const getAllRootCategoriesAreSelected = () => {
        return selectedCategoriesSequences.length === categoriesTrees.length;
    }


    const [materials, setMaterials] = useState([]);
    const [selectedMaterialsIds, setSelectedMaterialsIds] = useState([]);

    useEffect(() => {
        Api.getAllMaterials()
            .then(retrievedMaterials => setMaterials(retrievedMaterials))
            .catch(() => alert("Ошибка при получении списка материалов, свяжитесь с разработчиком"));
    }, []);

    const handleAddMaterial = (e) => {
        e.preventDefault();

        for (let i = 0; i < materials.length; i++) {
            if(!getMaterialIsSelectedById(materials[i]["id"])) {
                const updatedList = structuredClone(selectedMaterialsIds);
                updatedList.push(materials[i]["id"]);
                setSelectedMaterialsIds(updatedList);
                return;
            }
        }
    }

    const handleChangeMaterialSelect = (e) => {
        e.preventDefault();

        const chosenSelectIndex = e.target.selectedIndex;
        const chosenMaterialId = Number(e.target.options[chosenSelectIndex].value);

        const currentlySelectedMaterialId = Number(e.target.options[0].value);

        if(getMaterialIsSelectedById(chosenMaterialId)) {
            alert("Ошибка: материал " + getCategoryById(chosenMaterialId)["name"] + " уже выбран!");
            return;
        }

        const indexOfCurrentlySelectedMaterialId = selectedMaterialsIds.indexOf(currentlySelectedMaterialId);

        const updatedList = structuredClone(selectedMaterialsIds);
        updatedList[indexOfCurrentlySelectedMaterialId] = chosenMaterialId;
        setSelectedMaterialsIds(updatedList);
    }

    const getUnselectedMaterials = () => {
        
        const unselectedMaterials = [];

        for (let i = 0; i < materials.length; i++) {
            if(!getMaterialIsSelectedById(materials[i]["id"])) {
                unselectedMaterials.push(materials[i]);
            }
        }

        return unselectedMaterials;
    }

    const getMaterialIsSelectedById = (id) => {
        return selectedMaterialsIds.indexOf(id) !== -1;
    }

    const getMaterialById = (id) => {
        for (let i = 0; i < materials.length; i++) {
            if(materials[i]["id"] === id) return materials[i];
        }
    }

    const handleRemoveChosenMaterial = (e, id) => {
        e.preventDefault();

        const updatedList = structuredClone(selectedMaterialsIds);
        updatedList.splice(updatedList.indexOf(id), 1);
        setSelectedMaterialsIds(updatedList);
    }

    const getAllMaterialsAreSelected = () => {
        return selectedMaterialsIds.length === materials.length;
    }


    const [colors, setColors] = useState([]);
    const [selectedColorsIds, setSelectedColorsIds] = useState([]);

    useEffect(() => {
        Api.getAllColors()
            .then(retrievedColors => setColors(retrievedColors))
            .catch(() => alert("Ошибка при получении списка цветов, свяжитесь с разработчиком"));
    }, []);

    const getColorById = (id) => {
        for (let i = 0; i < colors.length; i++) {
            if(colors[i]["id"] === id) return colors[i];
        }
    }

    const getAllColorsAreSelected = () => {
        return selectedColorsIds.length === colors.length;
    }

    const handleAddColor = (e) => {
        e.preventDefault();

        for (let i = 0; i < colors.length; i++) {
            if(!getColorIsSelectedById(colors[i]["id"])) {
                const updatedList = structuredClone(selectedColorsIds);
                updatedList.push(colors[i]["id"]);
                setSelectedColorsIds(updatedList);
                return;
            }
        }
    }

    const handleRemoveChosenColor = (e, id) => {
        e.preventDefault();

        const updatedList = structuredClone(selectedColorsIds);
        updatedList.splice(updatedList.indexOf(id), 1);
        setSelectedColorsIds(updatedList);
    }

    const getUnselectedColors = () => {

        const unselectedColors = [];

        for (let i = 0; i < colors.length; i++) {
            if(!getColorIsSelectedById(colors[i]["id"])) {
                unselectedColors.push(colors[i]);
            }
        }

        return unselectedColors;
    }

    const handleChangeColorSelect = (e) => {
        e.preventDefault();

        const chosenSelectIndex = e.target.selectedIndex;
        const chosenColorId = Number(e.target.options[chosenSelectIndex].value);

        const currentlySelectedColorId = Number(e.target.options[0].value);

        if(getColorIsSelectedById(chosenColorId)) {
            alert("Ошибка: цвет " + getCategoryById(chosenColorId)["name"] + " уже выбран!");
            return;
        }

        const indexOfCurrentlySelectedColorId = selectedColorsIds.indexOf(currentlySelectedColorId);

        const updatedList = structuredClone(selectedColorsIds);
        updatedList[indexOfCurrentlySelectedColorId] = chosenColorId;
        setSelectedColorsIds(updatedList);
    }

    const getColorIsSelectedById = (id) => {
        return selectedColorsIds.indexOf(id) !== -1;
    }


    const [sizes, setSizes] = useState([]);
    const [sizesQuantities, setSizesQuantities] = useState([]);

    useEffect(() => {
        Api.getAllSizes()
            .then(retrievedSizes => setSizes(retrievedSizes))
            .catch(() => alert("Ошибка при получении списка существующих размеров, свяжитесь с разработчиком"));
    }, []);

    const handleAddSizeQuantity = (e) => {
        e.preventDefault();
        const sizeQuantity = {size: getUnselectedSize(), quantity: ""};

        const updatedList = structuredClone(sizesQuantities);
        updatedList.push(sizeQuantity);
        setSizesQuantities(updatedList);
    }

    const getUnselectedSize = () => {

        for (let i = 0; i < sizes.length; i++) {
            if(!getSizeIsSelectedById(sizes[i]["id"])) return sizes[i];
        }

        return null;
    }

    const getUnselectedSizes = () => {

        const unselectedSizes = [];

        for (let i = 0; i < sizes.length; i++) {
            if(!getSizeIsSelectedById(sizes[i]["id"])) {
                unselectedSizes.push(sizes[i]);
            }
        }

        return unselectedSizes;
    }

    const getSizeIsSelectedById = (id) => {

        for (let j = 0; j < sizesQuantities.length; j++) {
            if(sizesQuantities[j]["size"]["id"] === id) {
                return true;
            }
        }

        return false;
    }

    const getAllSizesAreSelected = () => {
        return sizesQuantities.length === sizes.length;
    }

    const handleSizeQuantityQuantityChange = (e, sizeId) => {
        e.preventDefault();

        const updatedValue = e.target.value;

        const updatedSizesQuantities = structuredClone(sizesQuantities);

        for (let i = 0; i < updatedSizesQuantities.length; i++) {
            if(updatedSizesQuantities[i]["size"]["id"] === sizeId) {
                updatedSizesQuantities[i]["quantity"] = updatedValue;
                setSizesQuantities(updatedSizesQuantities);
                return;
            }
        }
    }

    const handleChangeSizeQuantitySelect = (e) => {
        e.preventDefault();

        const chosenSelectIndex = e.target.selectedIndex;
        const chosenSizeId = Number(e.target.options[chosenSelectIndex].value);

        const currentlySelectedSizeId = Number(e.target.options[0].value);

        if(getSizeIsSelectedById(chosenSizeId)) {
            alert("Ошибка: размер " + getSizeById(chosenSizeId)["name"] + " уже выбран!");
            return;
        }

        const updatedSizesQuantities = structuredClone(sizesQuantities);

        for (let i = 0; i < updatedSizesQuantities.length; i++) {
            if(updatedSizesQuantities[i]["size"]["id"] === currentlySelectedSizeId) {
                updatedSizesQuantities[i]["size"] = getSizeById(chosenSizeId);
                setSizesQuantities(updatedSizesQuantities);
                return;
            }
        }
    }

    const getSizeById = (id) => {
        for (let i = 0; i < sizes.length; i++) {
            if(sizes[i]["id"] === id) {
                return sizes[i];
            }
        }

        return null;
    }

    const handleRemoveSizeQuantity = (e, id) => {
        e.preventDefault();

        const updatedSizesQuantities = [];

        for (let i = 0; i < sizesQuantities.length; i++) {
            if(sizesQuantities[i]["size"]["id"] !== id) updatedSizesQuantities.push(sizesQuantities[i]);
        }
        
        return updatedSizesQuantities;
    }


    const [imagesData, setImagesData] = useState([]);
    const [ongoingUploadImagesNames, setOngoingUploadImagesNames] = useState([]);
    const [imageUploaderRef] = useState(useRef());

    const handleAddImageInput = (e) => {
        e.preventDefault();
        imageUploaderRef.current.click();
    }

    const handleImageUpload = (e) => {

        e.preventDefault();

        const image = e.target.files[0];

        if(!image) {
            return;
        }

        addOngoingUploadImageName(image["name"]);

        Api.uploadImage(image, new AbortController().signal)
            .then(uploadedImage => {
                removeOngoingUploadImageName(image["name"]);
                const updatedImagesData = structuredClone(imagesData);
                updatedImagesData.push({
                    uploadedImage,
                    name: image["name"]
                });
                setImagesData(updatedImagesData);
            })
            .catch(() => {
                removeOngoingUploadImageName(image["name"]);
                alert("Ошибка при попытке загрузить изображение, свяжитесь с разработчиком");
            });
    }

    const addOngoingUploadImageName = (name) => {
        const updatedOngoingUploadImagesNames = structuredClone(ongoingUploadImagesNames);
        updatedOngoingUploadImagesNames.push(name);
        setOngoingUploadImagesNames(updatedOngoingUploadImagesNames);
    };

    const removeOngoingUploadImageName = (name) => {
        const updatedOngoingUploadImagesNames = structuredClone(ongoingUploadImagesNames);
        updatedOngoingUploadImagesNames.splice(updatedOngoingUploadImagesNames.indexOf(name), 1);
        setOngoingUploadImagesNames(updatedOngoingUploadImagesNames);
    }


    const [name, setName] = useState("");
    const [description, setDescription] = useState("");
    const [price, setPrice] = useState("");
    const [hasPrint, setHasPrint] = useState(false);


    const [fieldErrors, setFieldErrors] = useState([]);

    const handleCreateItem = (e) => {
        e.preventDefault();

        const imagesIds = [];
        for (let i = 0; i < imagesData.length; i++) imagesIds.push(imagesData[i]["uploadedImage"]["id"]);

        const categoriesIds = [];
        for (let i = 0; i < selectedCategoriesSequences.length; i++)
            categoriesIds.push(...selectedCategoriesSequences[i]);

        const sizesQuantitiesDtos = [];
        for (let i = 0; i < sizesQuantities.length; i++)
            sizesQuantitiesDtos.push({
                sizeId: sizesQuantities[i]["size"]["id"],
                quantity: Number(sizesQuantities[i]["quantity"])
            });


        const createItemRequestDto = {
            name, description, price, hasPrint, imagesIds, categoriesIds,
            materialsIds: selectedMaterialsIds,
            colorsIds: selectedColorsIds,
            sizesQuantities: sizesQuantitiesDtos
        };

        Api.createItem(createItemRequestDto, new AbortController().signal)
            .then(responseBody => {
                if(responseBody["id"]) {
                    window.location.href = "/item/" + responseBody["id"];
                }
            }).catch(e => {
                if(e instanceof InvalidEntityException) {
                    setFieldErrors(e.validationResult.fieldErrors);
                }
            });
    }

    return (
        <div className={"CreateItemPage"}>
            <div className="section no-margin">
                <h1 className={"page-title"}>Создание товара</h1>
            </div>

            <div className="section block-title">Основное</div>
            <div className="section form-row no-margin">
                <label htmlFor="name">Название</label>
                <input className="flct-input"
                       value={name}
                       onChange={(e) => setName(e.target.value)}
                       type="text"
                       id={"name"}
                       placeholder={"Название товара"}/>
            </div>
            <div className="section form-row no-margin">
                <label htmlFor="description">Описание</label>
                <input className="flct-input"
                       value={description}
                       onChange={(e) => setDescription(e.target.value)}
                       type="text"
                       id={"description"}
                       placeholder={"Описание товара"}/>
            </div>
            <div className="section form-row">
                <label htmlFor="price">Цена (₽)</label>
                <input className="flct-input"
                       value={price}
                       onChange={(e) => setPrice(e.target.value)}
                       type="number"
                       id={"price"}
                       placeholder={"Цена товара"}/>
            </div>

            <div className="section block-title">Фотографии</div>
            <div className="section form-row col">

                <input type="file"
                       id="file-uploader"
                       onChange={(e) => handleImageUpload(e)}
                       ref={imageUploaderRef}/>

                {imagesData.length === 0 && <p>Фотографии не добавлены</p>}
                {imagesData.length !== 0 && imagesData.map((imageData) => {
                    return (
                        <div key={imageData["uploadedImage"]["id"]} className="selects-row">
                            <img src={Api.getBaseApiUrl() + "/image/" + imageData["uploadedImage"]["id"]} />
                            <span>{imageData["name"]}</span><span className="status-ready">загружена</span>
                        </div>
                    );
                })}
                {ongoingUploadImagesNames.length !== 0 && ongoingUploadImagesNames.map((name, index)=> {
                    return (
                        <div className="selects-row" key={name + index}>
                            <span>{name}</span><span className="status-uploading">загружается</span>
                        </div>
                    );
                })}

                <a href="#"
                   className="link"
                   onClick={(e) => handleAddImageInput(e)}>Добавить</a>
            </div>

            <div className="section block-title">Категории</div>
            <div className="section form-row col">
                {selectedCategoriesSequences.length === 0 && <p>Категории не указаны</p>}
                {selectedCategoriesSequences.length > 0 && selectedCategoriesSequences.map(sequence => {
                    return (
                        <div key={sequence[0]} className={"selects-row"}>
                            {sequence.map(categoryId => {

                                return (
                                    <React.Fragment key={categoryId}>
                                        <select className="flct-input"
                                                value={categoryId}
                                                onChange={(e) => handleChangeCategorySelect(e)}>

                                            <option className="flct-input"
                                                    value={categoryId}>{getCategoryById(categoryId)["name"]}</option>
                                            {
                                                getUnselectedCategorySiblingsById(categoryId).map(sibling => {
                                                    return (
                                                        <option className="flct-input"
                                                                key={sibling["id"]}
                                                                value={sibling["id"]}>{sibling["name"]}</option>
                                                    );
                                                })
                                            }
                                        </select>
                                        {sequence.indexOf(categoryId) !== sequence.length - 1 && " > "}
                                    </React.Fragment>
                                );
                            })}
                            {getHasSubCategoriesById(sequence[sequence.length - 1]) &&
                                <a href="#"
                                   className="link"
                                   onClick={(e) => handleAddSubCategorySelect(e, sequence[sequence.length - 1])}>
                                    Уточнить
                                </a>
                            }
                            <a href="#"
                               className="link danger"
                               onClick={(e) => handleReduceCategoriesSequenceLength(e, sequence[0])}>Убрать</a>
                        </div>
                    );
                })}

                {!getAllRootCategoriesAreSelected() &&
                    <a href="#"
                       className="link"
                       onClick={(e) => handleAddRootCategorySelect(e)}>Добавить</a>
                }
            </div>

            <div className="section block-title">Состав (материалы)</div>
            <div className="section form-row col">
                {selectedMaterialsIds.length === 0 && <p>Состав не указан</p>}
                {selectedMaterialsIds.length > 0 && selectedMaterialsIds.map((materialId) => {
                    const {id, name} = getMaterialById(materialId);
                    return (
                        <div key={materialId} className={"selects-row"}>
                            <React.Fragment>
                                <select className="flct-input"
                                        value={materialId}
                                        onChange={(e) => handleChangeMaterialSelect(e)}>
                                    <option className="flct-input" value={id}>{name}</option>
                                    {getUnselectedMaterials().map((unselectedMaterial) => {
                                        return (
                                            <option className="flct-input"
                                                    key={unselectedMaterial["id"]}
                                                    value={unselectedMaterial["id"]}>
                                                {unselectedMaterial["name"]}
                                            </option>
                                        );
                                    })}
                                </select>
                                <a href="#"
                                   className="link danger"
                                   onClick={(e) => handleRemoveChosenMaterial(e, id)}>Убрать</a>
                            </React.Fragment>
                        </div>
                    );
                })}
                {!getAllMaterialsAreSelected() &&
                    <a href="#"
                       className="link"
                       onClick={(e) => handleAddMaterial(e)}>Добавить</a>
                }
            </div>

            <div className="section block-title">Цвета</div>
            <div className="section form-row col">
                {selectedColorsIds.length === 0 && <p>Цвета не указаны</p>}
                {selectedColorsIds.length > 0 && selectedColorsIds.map((colorId) => {
                    const {id, name} = getColorById(colorId);
                    return (
                        <div key={colorId} className={"selects-row"}>
                            <React.Fragment>
                                <select className="flct-input"
                                        value={colorId}
                                        onChange={(e) => handleChangeColorSelect(e)}>
                                    <option className="flct-input" value={id}>{name}</option>
                                    {getUnselectedColors().map((unselectedColor) => {
                                        return (
                                            <option className="flct-input"
                                                    key={unselectedColor["id"]}
                                                    value={unselectedColor["id"]}>
                                                {unselectedColor["name"]}
                                            </option>
                                        );
                                    })}
                                </select>
                                <a href="#"
                                   className="link danger"
                                   onClick={(e) => handleRemoveChosenColor(e, id)}>Убрать</a>
                            </React.Fragment>
                        </div>
                    );
                })}
                {!getAllColorsAreSelected() &&
                    <a href="#"
                       className="link"
                       onClick={(e) => handleAddColor(e)}>Добавить</a>
                }
            </div>

            <div className="section block-title">Размеры и наличие товара</div>
            <div className="section form-row col">
                {sizesQuantities.length === 0 && <p>Ничего не указано</p>}
                {sizesQuantities.length > 0 && sizesQuantities.map((sq) => {
                    return (
                        <div className="selects-row" key={sq["size"]["id"]}>
                            <select className="flct-input"
                                    value={sq["size"]["id"]}
                                    onChange={(e) => handleChangeSizeQuantitySelect(e)}>
                                <option className="flct-input"
                                        value={sq["size"]["id"]}>{sq["size"]["name"]}</option>
                                {getUnselectedSizes().map(unselectedSize => {
                                    return <option className="flct-input"
                                                   value={unselectedSize["id"]}
                                                   key={unselectedSize["id"]}>{unselectedSize["name"]}</option>
                                })}
                            </select>
                            <input className="flct-input"
                                   type="number"
                                   value={sq["quantity"]}
                                   placeholder="Количество (шт)"
                                   onChange={(e) => handleSizeQuantityQuantityChange(e, sq["size"]["id"])}/>
                            <a href="#"
                               className="link danger"
                               onClick={(e) => handleRemoveSizeQuantity(e, sq["size"]["id"])}>Убрать</a>
                        </div>
                    );
                })}
                {!getAllSizesAreSelected() &&
                    <a href="#"
                       className="link"
                       onClick={(e) => handleAddSizeQuantity(e)}>Добавить</a>
                }
            </div>

            <div className="section block-title">Прочее</div>
            <div className="section form-row">
                <label htmlFor="has-print">С принтом</label>
                <input type="checkbox"
                       id={"has-print"}
                       value={String(hasPrint)}
                       onChange={(e) => setHasPrint(Boolean(e.target.value))}
                />
            </div>

            <div className="section">
                <a className="button" onClick={(e) => handleCreateItem(e)}>Создать</a>
            </div>

            <div className="errors-container">
                {fieldErrors.map(fieldError => {
                    return <p key={fieldError.errorCode}>{fieldError.errorMessage}</p>
                })}
            </div>
        </div>
    );
}

export default withHeaderAndFooter(requiresUser(
    CreateItemPage,
    "Чтобы просмотреть эту страницу, нужно войти в аккаунт администратора.",
    true
));