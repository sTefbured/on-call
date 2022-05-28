import CollectionGrid from "../../../common/collectiongrid/CollectionGrid";
import React from "react";
import GroupInfoCard from "../GroupInfoCard";

const UserGroups = (props) => {
    let groupComponents = props.groups.map(group => (<div key={group.id}><GroupInfoCard group={group}/></div>));
    return (
        <div>
            <CollectionGrid totalCount={props.totalCount}
                            pageSize={props.pageSize}
                            currentPage={props.currentPage}
                            onPageChanged={props.onPageChanged}>
                {groupComponents}
            </CollectionGrid>
        </div>
    )
}

export default UserGroups;