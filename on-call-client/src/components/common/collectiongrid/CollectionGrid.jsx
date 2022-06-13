import styles from "./collectiongrid.module.css";
import React from "react";

const CollectionGrid = (props) => {
    let pagesCount = Math.ceil(props.totalCount / props.pageSize);
    let pageNumbers = [];
    for (let i = 0; i < pagesCount; i++) {
        let className = styles.pageNumber;
        className = props.currentPage === i ? className + ' ' + styles.currentPage : className;
        let pageNumber = <div onClick={() => {props.onPageChanged(i)}} key={i} className={className}>{i + 1}</div>;
        pageNumbers = [...pageNumbers, pageNumber];
    }
    return (
        <div>
            <div>{pageNumbers}</div>
            <div className={styles.list}>{props.children}</div>
        </div>
    )
}

export default CollectionGrid;