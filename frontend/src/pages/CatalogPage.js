import withHeaderAndFooter from "../hoc/withHeaderAndFooter";
import inCage from "../hoc/inCage";
import "../css/CatalogPage.css";
import React, {useState} from "react";
import ItemFiltersContainer from "../components/ItemFiltersContainer";
import Api from "../Api";
import {useSearchParams} from "react-router-dom";

function CatalogPage() {

    const [searchParams] = useSearchParams();
    if(searchParams.get("page") === null)
        searchParams.set("page", 0);

    const CatalogState = {
        LOADING: "LOADING",
        LOADED: "LOADED",
    }

    const [itemsPage, setItemsPage] = useState(null);
    const [catalogState, setCatalogState] = useState(CatalogState.LOADING);

    let pagesNumbers = [];
    if(itemsPage !== null) {
        for (let i = 0; i < itemsPage["paginationMeta"]["totalPages"]; i++) {
            pagesNumbers.push(i);
        }
    }

    const switchPage = (pageNumber) => {

        searchParams.set("page", pageNumber);
        window.location.href = window.location.href.split("?")[0] + "?" + searchParams;
    }

    return (
        <div className="CatalogPage">

            <ItemFiltersContainer setItemsPage={setItemsPage}
                                  setCatalogState={setCatalogState}
                                  onSuccessCatalogState={CatalogState.LOADED}/>

            <div className="right-side">

                <div className="heading">Каталог</div>

                {catalogState === CatalogState.LOADED && itemsPage["items"].length > 0 &&
                    <div className="catalog-items">
                        {itemsPage["items"].map(item => {

                            const imageUrl = item["images"].length > 0 ?
                                Api.getImageUrlByImageId(item["images"][0]["id"]) :
                                "/ui/item-placeholder.png";

                            return (
                                <div key={item["id"]} className="item">
                                    <a href={"/item/" + item["id"]}>
                                        <div className="image-container">
                                            <img src={imageUrl} alt={item["name"]}/>
                                        </div>
                                        <p className="item-name">{item["name"]}</p>
                                        <p className="item-price">{item["price"]}₽</p>
                                    </a>
                                </div>
                            );
                        })}
                    </div>
                }
                {catalogState === CatalogState.LOADED && itemsPage["items"].length === 0 &&
                    <div className="catalog-items">Товары по вашему запросу не найдены</div>
                }


                {pagesNumbers.length > 0 &&
                    <div className="pagination">
                        <span className="pagination-title">Страницa:</span>
                        {pagesNumbers.map(pageNumber => {
                            if(pageNumber === Number(searchParams.get("page"))) {
                                return <span key={pageNumber}>{pageNumber + 1}</span>
                            }

                            return (
                                <span key={pageNumber}
                                      className={"link"}
                                      onClick={() => switchPage(pageNumber)}>
                            {pageNumber + 1}
                        </span>
                            );
                        })}
                    </div>
                }



            </div>
        </div>
    )
}

export default inCage(withHeaderAndFooter(CatalogPage));