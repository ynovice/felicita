import withHeaderAndFooter from "../hoc/withHeaderAndFooter";
import requiresUser from "../hoc/requiresUser";
import "../css/CreateArticlePage.css";
import React, {useEffect, useMemo, useState} from "react";
import EditorJS from '@editorjs/editorjs';
import Api from "../Api";
import edjsHtml from "editorjs-html";
import InvalidEntityException from "../exception/InvalidEntityException";
import {useSearchParams} from "react-router-dom";

function CreateArticlePage() {

    const [searchParams] = useSearchParams();

    const [name, setName] = useState("");
    const [nameAreaRef] = useState(React.createRef());

    useEffect(() => {
        nameAreaRef.current.style.height = "5px";
        nameAreaRef.current.style.height = (nameAreaRef.current.scrollHeight) + "px";

    }, [nameAreaRef, name])

    const DOMPurify = useMemo(() => require("dompurify")(window), []);
    const [editor, setEditor] = useState(null);

    useEffect(() => {

        const abortController = new AbortController();

        const tools = {
            header: require('@editorjs/header'),
            image: {
                class: require("@editorjs/image"),
                config: {
                    uploader: {
                        uploadByFile(file) {
                            return Api.uploadImage(file).then((imageDto) => {
                                return {
                                    success: 1,
                                    file: {
                                        url: Api.getImageUrlByImageId(imageDto["id"])
                                    }
                                }
                            });
                        },
                        uploadByUrl(url) {
                            return new Promise((resolve) => {
                                resolve ({
                                    success: 1,
                                    file: {
                                        url
                                    }
                                });
                            });
                        }
                    }
                }

            }
        }

        setEditor(currentEditor => {

            if(currentEditor) {

                const editedArticleId = searchParams.get("id");
                if(editedArticleId) {
                    Api.getArticleById(Number(editedArticleId))
                        .then(article => {

                            setName(article["name"]);

                            (function loopedEditorUpdater() {
                                setTimeout(function () {
                                    if(!currentEditor.blocks) {
                                        loopedEditorUpdater();
                                    } else {
                                        currentEditor.blocks.renderFromHTML(article["content"]);
                                    }
                                    console.log("exec")
                                }, 100);
                            })();
                        })
                        .catch((e) => {
                            console.log(e)
                            alert("Произошла ошибка при попытке загрузить редактируемую статью");
                        });
                }

                return currentEditor;
            }

            return new EditorJS({
                holder: "editor",
                placeholder: "Нажмите, чтобы начать писать статью",
                tools
            });
        });

        return () => abortController.abort();
    }, [searchParams]);

    const [fieldErrors, setFieldErrors] = useState([]);
    const handleSaveClick = () => {

        editor.save().then((outputData) => {

            const edjsParser = edjsHtml();
            let html = edjsParser.parse(outputData);

            if(searchParams.get("id")) {
                const requestDto = {
                    id: Number(searchParams.get("id")),
                    name: name,
                    content: DOMPurify.sanitize(html.join(""))
                };

                Api.updateArticle(requestDto)
                    .then(articleDto => {
                        window.location.href = "/article/" + articleDto["id"];
                    })
                    .catch(e => {
                        if(e instanceof InvalidEntityException) {
                            setFieldErrors(e.validationResult.fieldErrors);
                        }
                    });
            } else {
                const requestDto = {
                    name: name,
                    content: DOMPurify.sanitize(html.join(""))
                };

                Api.createArticle(requestDto)
                    .then(articleDto => {
                        window.location.href = "/article/" + articleDto["id"];
                    })
                    .catch(e => {
                        if(e instanceof InvalidEntityException) {
                            setFieldErrors(e.validationResult.fieldErrors);
                        }
                    });
            }

        }).catch((error) => {
            console.log('Saving failed: ', error)
        });
    }


    return (
        <div className="CreateArticlePage">
            <h1 className="page-title">Создание статьи</h1>

            <textarea ref={nameAreaRef}
                      onChange={(e) => setName(e.target.value)}
                      value={name}
                      className="article-name-editor"
                      placeholder="Название статьи"
                      cols="30"
                      rows="10">
            </textarea>

            <div id="editor"></div>

            <input type="button" className="button" value="Сохранить" onClick={() => handleSaveClick()}/>

            <div className="errors-container">
                {fieldErrors.map(fieldError => {
                    return <p key={fieldError.errorCode}>{fieldError.errorMessage}</p>
                })}
            </div>
        </div>
    );
}

export default withHeaderAndFooter(requiresUser(
    CreateArticlePage,
    "Чтобы просмотреть эту страницу, нужно войти в аккаунт администратора.",
    true
));