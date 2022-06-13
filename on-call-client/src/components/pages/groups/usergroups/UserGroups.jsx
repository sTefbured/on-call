import CollectionGrid from "../../../common/collectiongrid/CollectionGrid";
import React from "react";
import GroupInfoCard from "../GroupInfoCard";
import Button from "../../../common/button/Button";
import CreateNewGroupDialogContainer from "./CreateNewGroupDialogContainer";

const UserGroups = (props) => {
    let groupComponents = props.groups.map(group => (<div key={group.id}><GroupInfoCard group={group}/></div>));
    return (
        <div>
            <CreateNewGroupDialogContainer isActive={props.isNewGroupDialogOpened}
                                           setIsActive={props.setIsNewGroupDialogOpened}/>
            <Button onClick={() => props.setIsNewGroupDialogOpened(true)}>Create new group</Button>
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