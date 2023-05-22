import withHeaderAndFooter from "../hoc/withHeaderAndFooter";
import inCage from "../hoc/inCage";
import "../css/MainPage.css";
import {useEffect, useMemo, useState} from "react";
import Api from "../Api";
import ItemsCatalog from "../components/ItemsCatalog";

function MainPage() {

    // [[...sectionOneItems], [...sectionTwoItems]]
    const [sectionsItems, setSectionsItems] = useState([]);

    const sections = useMemo(() => [
        {
            name: "Женское",
            searchParams: {
                categoriesIds: 1
            }
        },
        {
            name: "Мужское",
            searchParams: {
                categoriesIds: 2
            }
        }
    ], []);

    useEffect(() => {

        const abortController = new AbortController();

        if(sections.length > sectionsItems.length) {


            Promise.all(
                sections.map(section =>
                    Api.getItemsPageByFilterParams(section.searchParams, abortController.signal)
                        .then(retrievedItemsPage => retrievedItemsPage["items"]))
            )
                .then(items => {
                    const updatedSectionsItems = [];
                    updatedSectionsItems.push(...items);
                    setSectionsItems(updatedSectionsItems);
                });
        }

        return () => abortController.abort();
    }, [sections, sectionsItems.length]);

    return (
        <div className={"MainPage"}>

            <a href="/catalog" className="banner-container">
                <img src="/ui/banner.png" alt="banner"/>
            </a>

            <div className="feedback-proposal">
                <div className="proposal-title">Оставьте заявку прямо сейчас - мы с вами свяжемся.</div>
                <input type="button" className="button" value="Обратная связь"/>
            </div>

            {sectionsItems.length > 0 &&
                sections.map((section, i) => {
                    return (
                        <div key={"section-" + section["name"]} className="section">
                            <div className="section-name">
                                {section["name"]}
                            </div>
                            <ItemsCatalog items={sectionsItems[i]} oneRow={true} justifyContent={"space-between"}/>
                            <a href={"/catalog?" + new URLSearchParams(section.searchParams)}
                               className="link section-link">
                                Смотреть ещё →
                            </a>
                        </div>
                    );
                })
            }


        </div>
    );
}

export default inCage(withHeaderAndFooter(MainPage));