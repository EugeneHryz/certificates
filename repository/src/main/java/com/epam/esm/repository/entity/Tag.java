package com.epam.esm.repository.entity;

import com.epam.esm.repository.entity.listener.AuditEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.NamedNativeQuery;
import java.util.Objects;

@NamedNativeQuery(name = "findWidelyUsedTagOfUserWithHighestSpending",
        query = "SELECT tag.id, name\n" +
                "FROM tag\n" +
                "         INNER JOIN (SELECT ct.tag_id as 'tagId', COUNT(*) AS frequency\n" +
                "                     FROM (SELECT tag_id\n" +
                "                           FROM certificate_tag_mapping\n" +
                "                                    INNER JOIN (SELECT certificate_id AS certId\n" +
                "                                                FROM certificate_order\n" +
                "                                                WHERE user_id = (SELECT us.user_id\n" +
                "                                                                 FROM (SELECT user_id, MAX(total) as total_spent\n" +
                "                                                                       FROM certificate_order\n" +
                "                                                                       GROUP BY user_id\n" +
                "                                                                       ORDER BY total_spent DESC\n" +
                "                                                                       LIMIT 1) us)) uc\n" +
                "                                               ON uc.certId = certificate_id) ct\n" +
                "                     GROUP BY ct.tag_id\n" +
                "                     ORDER BY frequency DESC\n" +
                "                     LIMIT 1) tf ON tf.tagId = tag.id;",
        resultClass = Tag.class)
@Entity
@EntityListeners(AuditEntityListener.class)
public class Tag extends AbstractEntity {

    @Column(nullable = false, unique = true)
    private String name;

    public Tag() {
    }

    public Tag(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tag)) return false;
        Tag tag = (Tag) o;
        return name.equals(tag.name) && getId() == tag.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), name);
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + getId() +
                "name='" + name + '\'' +
                '}';
    }
}
