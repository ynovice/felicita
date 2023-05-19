import {useSearchParams} from "react-router-dom";
import Api from "../Api";
import {useEffect, useState} from "react";
import "../css/ItemsFilter.css";

function ItemsFilter({setItemsPage, setCatalogState, onSuccessCatalogState}) {

    const [searchParams] = useSearchParams();

    if(searchParams.get("page") === null)
        searchParams.set("page", 0);

    useEffect(() => {

        const abortController = new AbortController();

        Api.getItemsPageByFilterParams(searchParams, abortController.signal)
            .then(itemsPage => {
                setItemsPage(itemsPage);
                setCatalogState(onSuccessCatalogState);
            });

        return () => abortController.abort();
    }, [onSuccessCatalogState, searchParams, setCatalogState, setItemsPage]);

    const [priceFrom, setPriceFrom] = useState(
        searchParams.get("priceFrom") !== null ? searchParams.get("priceFrom") :
            ""
    );

    const [priceTo, setPriceTo] = useState(
        searchParams.get("priceTo") !== null ? searchParams.get("priceTo") :
            ""
    );

    const [existingSizes, setExistingSizes] = useState([]);
    const [isSizeChosen, setIsSizeChosen] = useState([]);

    useEffect(() => {
        Api.getAllSizes()
            .then(retrievedSizes => {

                const updatedIsSizeChosen = [];

                console.log(retrievedSizes)
                for (let i = 0; i < retrievedSizes.length; i++) updatedIsSizeChosen.push(false);
                console.log(updatedIsSizeChosen);

                if(searchParams.get("sizesIds") !== null) {

                    let splitSizesIds = searchParams.get("sizesIds").split(",");
                    splitSizesIds.splice(splitSizesIds.length - 1, 1);

                    for (let i = 0; i < retrievedSizes.length; i++) {
                        for (let j = 0; j < splitSizesIds.length; j++) {

                            if(retrievedSizes[i]["id"] === Number(splitSizesIds[j])) {
                                updatedIsSizeChosen[i] = true;
                                console.log(j);
                                console.log(retrievedSizes[i])
                            }
                        }
                    }

                    console.log(splitSizesIds);
                }
                console.log(updatedIsSizeChosen)

                setExistingSizes(retrievedSizes);
                setIsSizeChosen(updatedIsSizeChosen);
            });
        }, [searchParams]);

    const choseSizeByIndex = (i) => {
        const updatedList = structuredClone(isSizeChosen);
        updatedList[i] = !updatedList[i];
        setIsSizeChosen(updatedList);
    }

    const handleApplyFiltersClick = () => {

        const searchParamsDto = buildNewSearchParams();

        window.location.href = window.location.href.split("?")[0] + "?" + new URLSearchParams(searchParamsDto);
    }

    const buildNewSearchParams = () => {

        const searchParamsDto = {};

        if(searchParams.get("page") !== null) searchParamsDto.page = searchParams.get("page");
        if(priceFrom !== "") searchParamsDto.priceFrom = priceFrom;
        if(priceTo !== "") searchParamsDto.priceTo = priceTo;


        let sizesIds = "";
        for (let i = 0; i < isSizeChosen.length; i++) {
            if (isSizeChosen[i]) {
                sizesIds = sizesIds + existingSizes[i]["id"] + ",";
            }
        }
        if(sizesIds !== "") searchParamsDto.sizesIds = sizesIds;

        return searchParamsDto;
    }

    return (
        <div className="ItemsFilter">
            <div className="title">Фильтры</div>

            <div className="inputs-filter">
                <div className="filter-name">Цена</div>
                <div>
                    <label htmlFor="priceFrom">От (₽)</label>
                    <input id="priceFrom"
                           type="number"
                           value={priceFrom}
                           onChange={(e) => setPriceFrom(e.target.value)}/>
                </div>
                <div>
                    <label htmlFor="priceTo">До (₽)</label>
                    <input id="priceTo"
                           type="number"
                           value={priceTo}
                           onChange={(e) => setPriceTo(e.target.value)}/>
                </div>
            </div>

            <div className="checkboxes-filter">
                <div className="filter-name">Размер</div>
                <div className="filter-values">
                    {existingSizes.map((existingSize, i) => {

                        console.log(existingSize, isSizeChosen[i])
                        return (
                            <div key={existingSize["id"]} className="value-row">
                                <input id={existingSize["id"]} type="checkbox" checked={isSizeChosen[i]}  onClick={() => choseSizeByIndex(i)}/>
                                <label htmlFor={existingSize["id"]}>{existingSize["name"]}</label>
                            </div>
                        );
                    })}
                </div>
            </div>

            <input type="button"
                   className="button"
                   value="Применить"
                   onClick={() => handleApplyFiltersClick()}/>

            <span onClick={() => window.location.href = window.location.href.split("?")[0]}
                  className="link danger">Сбросить</span>
        </div>
    );
}

export default ItemsFilter;