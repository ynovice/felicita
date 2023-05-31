import withHeaderAndFooter from "../hoc/withHeaderAndFooter";
import requiresUser from "../hoc/requiresUser";
import "../css/CreateItemPage.css";
import React, {useEffect, useRef, useState} from "react";
import Api from "../Api";
import InvalidEntityException from "../exception/InvalidEntityException";
import adminAccessOnly from "../hoc/adminAccessOnly";
import ItemPropertySelector from "../components/ItemPropertySelector";
import SizesQuantitiesSelector from "../components/SizesQuantitiesSelector";
import ItemCategoriesSelector from "../components/ItemCategoriesSelector";

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


    const [materials, setMaterials] = useState([]);
    const [selectedMaterialsIds, setSelectedMaterialsIds] = useState([]);

    useEffect(() => {
        Api.getAllMaterials()
            .then(retrievedMaterials => setMaterials(retrievedMaterials))
            .catch(() => alert("Ошибка при получении списка материалов, свяжитесь с разработчиком"));
    }, []);


    const [colors, setColors] = useState([]);
    const [selectedColorsIds, setSelectedColorsIds] = useState([]);

    useEffect(() => {
        Api.getAllColors()
            .then(retrievedColors => setColors(retrievedColors))
            .catch(() => alert("Ошибка при получении списка цветов, свяжитесь с разработчиком"));
    }, []);


    const [sizes, setSizes] = useState([]);
    const [sizesQuantities, setSizesQuantities] = useState([]);

    useEffect(() => {
        Api.getAllSizes()
            .then(retrievedSizes => setSizes(retrievedSizes))
            .catch(() => alert("Ошибка при получении списка существующих размеров, свяжитесь с разработчиком"));
    }, []);


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
                            <img src={Api.getBaseApiUrl() + "/image/" + imageData["uploadedImage"]["id"]}
                                 alt={imageData["uploadedImage"]["id"]}/>
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

                <span className="link"
                      onClick={(e) => handleAddImageInput(e)}>
                    Добавить
                </span>
            </div>

            <div className="section block-title">Категории</div>
            <ItemCategoriesSelector emptyListMessage="Категории не указаны"
                                    existingCategoriesTrees={categoriesTrees}
                                    selectedCategoriesSequences={selectedCategoriesSequences}
                                    setSelectedCategoriesSequences={setSelectedCategoriesSequences}/>

            <div className="section block-title">Состав (материалы)</div>
            <ItemPropertySelector emptyListMessage="Состав не указан"
                                  selectedIds={selectedMaterialsIds}
                                  setSelectedIds={setSelectedMaterialsIds}
                                  propertySource={materials}/>

            <div className="section block-title">Цвета</div>
            <ItemPropertySelector emptyListMessage="Цвета не указаны"
                                  selectedIds={selectedColorsIds}
                                  setSelectedIds={setSelectedColorsIds}
                                  propertySource={colors}/>

            <div className="section block-title">Размеры и наличие товара</div>
            <SizesQuantitiesSelector emptyListMessage="Ничего не указано"
                                     selectedSizesQuantities={sizesQuantities}
                                     setSelectedSizesQuantities={setSizesQuantities}
                                     existingSizes={sizes}/>

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
                <input type="button"
                       className="button"
                       onClick={(e) => handleCreateItem(e)}
                       value="Создать"/>
            </div>

            <div className="errors-container">
                {fieldErrors.map(fieldError => {
                    return <p key={fieldError.errorCode}>{fieldError.errorMessage}</p>
                })}
            </div>
        </div>
    );
}

export default withHeaderAndFooter(adminAccessOnly(requiresUser(
    CreateItemPage,
    "Чтобы просмотреть эту страницу, нужно войти в аккаунт администратора."
)));