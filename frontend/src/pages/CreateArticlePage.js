import inCage from "../hoc/inCage";
import withHeaderAndFooter from "../hoc/withHeaderAndFooter";
import requiresUser from "../hoc/requiresUser";
import "../css/CreateArticlePage.css";
import React, {useEffect, useMemo, useState} from "react";
import EditorJS from '@editorjs/editorjs';
import Api from "../Api";
import edjsHtml from "editorjs-html";
import InvalidEntityException from "../exception/InvalidEntityException";

function CreateArticlePage() {

    const [name, setName] = useState("");
    const [nameAreaRef] = useState(React.createRef());

    useEffect(() => {
        nameAreaRef.current.style.height = "5px";
        nameAreaRef.current.style.height = (nameAreaRef.current.scrollHeight) + "px";

    }, [nameAreaRef])

    const DOMPurify = useMemo(() => require("dompurify")(window), []);
    const [editor, setEditor] = useState(null);

    useEffect(() => {

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

        setEditor(value =>
            value ?
                value :
                new EditorJS({
                    holder: "editor",
                    placeholder: "Нажмите, чтобы начать писать статью",
                    tools
                })
        );
    }, []);

    const [fieldErrors, setFieldErrors] = useState([]);
    const handleSaveClick = () => {

        editor.save().then((outputData) => {

            const edjsParser = edjsHtml();
            let html = edjsParser.parse(outputData);

            const requestDto = {
                name: name,
                content: DOMPurify.sanitize(html.join(""))
            }

            Api.createArticle(requestDto)
                .then(articleDto => {
                    window.location.href = "/article/" + articleDto["id"];
                })
                .catch(e => {
                    if(e instanceof InvalidEntityException) {
                        setFieldErrors(e.validationResult.fieldErrors);
                    }
                });

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

export default inCage(withHeaderAndFooter(requiresUser(
    CreateArticlePage,
    "Чтобы просмотреть эту страницу, нужно войти в аккаунт администратора.",
    true
)))