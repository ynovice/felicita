import inCage from "../hoc/inCage";
import withHeaderAndFooter from "../hoc/withHeaderAndFooter";
import "../css/ArticlePage.css";
import {useContext, useEffect, useMemo, useState} from "react";
import ErrorPage from "./ErrorPage";
import {useParams} from "react-router-dom";
import Api from "../Api";
import NotFoundException from "../exception/NotFoundException";
import {AccessLevel, AppContext} from "../contexts/AppContext";
import RequestAbortedException from "../exception/RequestAbortedException";

function ArticlePage () {

    const { accessLevel } = useContext(AppContext);

    const ArticleState = {
        LOADING: "LOADING",
        PRESENT: "PRESENT",
        NOT_FOUND: "NOT_FOUND",
        ERROR: "ERROR"
    };

    const DOMPurify = useMemo(() => require("dompurify")(window), []);

    const { id } = useParams();

    const [article, setArticle] = useState(null);
    const [articleState, setArticleState] = useState(ArticleState.LOADING);

    useEffect(() => {

        const abortController = new AbortController();

        Api.getArticleById(id, abortController.signal)
            .then(retrievedArticle => {
                setArticle(retrievedArticle);
                setArticleState(ArticleState.PRESENT)
            })
            .catch(e => {
                if(e instanceof NotFoundException) {
                    setArticleState(ArticleState.NOT_FOUND);
                } else if (!(e instanceof RequestAbortedException)) {
                    setArticleState(ArticleState.ERROR);
                }
            });

        return () => abortController.abort();
    }, [ArticleState.ERROR, ArticleState.NOT_FOUND, ArticleState.PRESENT, id])

    if(articleState === ArticleState.LOADING) {
        return;
    } else if (articleState === ArticleState.NOT_FOUND) {
        return <ErrorPage errorMessage="Статья, которую вы пытаетесь открыть, не существует."/>
    } else if (articleState === ArticleState.ERROR) {
        return <ErrorPage errorMessage="Произошла какая-то ошибка при попытке открыть статью."/>
    }


    return (
        <div className="ArticlePage">
            <div className="article">
                <div className="name">{article["name"]}</div>
                <div className="content" dangerouslySetInnerHTML={{__html: DOMPurify.sanitize(article["content"])}}></div>
                <div className="article-meta">
                    <div className="author">Автор: {article["author"]}</div>
                    <div className="author">Создана {article["createdAtPresentation"]}</div>
                </div>
                {accessLevel === AccessLevel.ADMIN &&
                    <div><a href={"/admin/article/create?id=" + article["id"]} className="link">Редактировать →</a></div>
                }
            </div>

            <div className="similar-articles">
                <div className="section-title">Другие статьи:</div>
                <p>Секция в разработке...</p>
            </div>
        </div>
    );
}

export default inCage(withHeaderAndFooter(ArticlePage));