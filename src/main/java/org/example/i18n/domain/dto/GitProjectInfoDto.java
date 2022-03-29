package org.example.i18n.domain.dto;

import java.util.List;

/**
 * @author wangyichun
 * @since 2022/1/10 8:17
 */
public class GitProjectInfoDto {
    private int id;
    private String description;
    private String name;
    private String name_with_namespace;
    private String path;
    private String path_with_namespace;
    private String created_at;
    private String default_branch;
    private String ssh_url_to_repo;
    private String http_url_to_repo;
    private String web_url;
    private String readme_url;
    private Object avatar_url;
    private int forks_count;
    private int star_count;
    private String last_activity_at;
    private NamespaceBean namespace;
    private LinksBean _links;
    private boolean packages_enabled;
    private boolean empty_repo;
    private boolean archived;
    private String visibility;
    private boolean resolve_outdated_diff_discussions;
    private boolean container_registry_enabled;
    private ContainerExpirationPolicyBean container_expiration_policy;
    private boolean issues_enabled;
    private boolean merge_requests_enabled;
    private boolean wiki_enabled;
    private boolean jobs_enabled;
    private boolean snippets_enabled;
    private boolean service_desk_enabled;
    private Object service_desk_address;
    private boolean can_create_merge_request_in;
    private String issues_access_level;
    private String repository_access_level;
    private String merge_requests_access_level;
    private String forking_access_level;
    private String wiki_access_level;
    private String builds_access_level;
    private String snippets_access_level;
    private String pages_access_level;
    private String operations_access_level;
    private String analytics_access_level;
    private Object emails_disabled;
    private boolean shared_runners_enabled;
    private boolean lfs_enabled;
    private int creator_id;
    private String import_status;
    private int open_issues_count;
    private int ci_default_git_depth;
    private boolean ci_forward_deployment_enabled;
    private boolean public_jobs;
    private int build_timeout;
    private String auto_cancel_pending_pipelines;
    private Object build_coverage_regex;
    private Object ci_config_path;
    private boolean only_allow_merge_if_pipeline_succeeds;
    private Object allow_merge_on_skipped_pipeline;
    private boolean restrict_user_defined_variables;
    private boolean request_access_enabled;
    private boolean only_allow_merge_if_all_discussions_are_resolved;
    private boolean remove_source_branch_after_merge;
    private boolean printing_merge_request_link_enabled;
    private String merge_method;
    private Object suggestion_commit_message;
    private boolean auto_devops_enabled;
    private String auto_devops_deploy_strategy;
    private boolean autoclose_referenced_issues;
    private PermissionsBean permissions;
    private List<?> tag_list;
    private List<?> shared_with_groups;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_with_namespace() {
        return name_with_namespace;
    }

    public void setName_with_namespace(String name_with_namespace) {
        this.name_with_namespace = name_with_namespace;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath_with_namespace() {
        return path_with_namespace;
    }

    public void setPath_with_namespace(String path_with_namespace) {
        this.path_with_namespace = path_with_namespace;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getDefault_branch() {
        return default_branch;
    }

    public void setDefault_branch(String default_branch) {
        this.default_branch = default_branch;
    }

    public String getSsh_url_to_repo() {
        return ssh_url_to_repo;
    }

    public void setSsh_url_to_repo(String ssh_url_to_repo) {
        this.ssh_url_to_repo = ssh_url_to_repo;
    }

    public String getHttp_url_to_repo() {
        return http_url_to_repo;
    }

    public void setHttp_url_to_repo(String http_url_to_repo) {
        this.http_url_to_repo = http_url_to_repo;
    }

    public String getWeb_url() {
        return web_url;
    }

    public void setWeb_url(String web_url) {
        this.web_url = web_url;
    }

    public String getReadme_url() {
        return readme_url;
    }

    public void setReadme_url(String readme_url) {
        this.readme_url = readme_url;
    }

    public Object getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(Object avatar_url) {
        this.avatar_url = avatar_url;
    }

    public int getForks_count() {
        return forks_count;
    }

    public void setForks_count(int forks_count) {
        this.forks_count = forks_count;
    }

    public int getStar_count() {
        return star_count;
    }

    public void setStar_count(int star_count) {
        this.star_count = star_count;
    }

    public String getLast_activity_at() {
        return last_activity_at;
    }

    public void setLast_activity_at(String last_activity_at) {
        this.last_activity_at = last_activity_at;
    }

    public NamespaceBean getNamespace() {
        return namespace;
    }

    public void setNamespace(NamespaceBean namespace) {
        this.namespace = namespace;
    }

    public LinksBean get_links() {
        return _links;
    }

    public void set_links(LinksBean _links) {
        this._links = _links;
    }

    public boolean isPackages_enabled() {
        return packages_enabled;
    }

    public void setPackages_enabled(boolean packages_enabled) {
        this.packages_enabled = packages_enabled;
    }

    public boolean isEmpty_repo() {
        return empty_repo;
    }

    public void setEmpty_repo(boolean empty_repo) {
        this.empty_repo = empty_repo;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public boolean isResolve_outdated_diff_discussions() {
        return resolve_outdated_diff_discussions;
    }

    public void setResolve_outdated_diff_discussions(boolean resolve_outdated_diff_discussions) {
        this.resolve_outdated_diff_discussions = resolve_outdated_diff_discussions;
    }

    public boolean isContainer_registry_enabled() {
        return container_registry_enabled;
    }

    public void setContainer_registry_enabled(boolean container_registry_enabled) {
        this.container_registry_enabled = container_registry_enabled;
    }

    public ContainerExpirationPolicyBean getContainer_expiration_policy() {
        return container_expiration_policy;
    }

    public void setContainer_expiration_policy(ContainerExpirationPolicyBean container_expiration_policy) {
        this.container_expiration_policy = container_expiration_policy;
    }

    public boolean isIssues_enabled() {
        return issues_enabled;
    }

    public void setIssues_enabled(boolean issues_enabled) {
        this.issues_enabled = issues_enabled;
    }

    public boolean isMerge_requests_enabled() {
        return merge_requests_enabled;
    }

    public void setMerge_requests_enabled(boolean merge_requests_enabled) {
        this.merge_requests_enabled = merge_requests_enabled;
    }

    public boolean isWiki_enabled() {
        return wiki_enabled;
    }

    public void setWiki_enabled(boolean wiki_enabled) {
        this.wiki_enabled = wiki_enabled;
    }

    public boolean isJobs_enabled() {
        return jobs_enabled;
    }

    public void setJobs_enabled(boolean jobs_enabled) {
        this.jobs_enabled = jobs_enabled;
    }

    public boolean isSnippets_enabled() {
        return snippets_enabled;
    }

    public void setSnippets_enabled(boolean snippets_enabled) {
        this.snippets_enabled = snippets_enabled;
    }

    public boolean isService_desk_enabled() {
        return service_desk_enabled;
    }

    public void setService_desk_enabled(boolean service_desk_enabled) {
        this.service_desk_enabled = service_desk_enabled;
    }

    public Object getService_desk_address() {
        return service_desk_address;
    }

    public void setService_desk_address(Object service_desk_address) {
        this.service_desk_address = service_desk_address;
    }

    public boolean isCan_create_merge_request_in() {
        return can_create_merge_request_in;
    }

    public void setCan_create_merge_request_in(boolean can_create_merge_request_in) {
        this.can_create_merge_request_in = can_create_merge_request_in;
    }

    public String getIssues_access_level() {
        return issues_access_level;
    }

    public void setIssues_access_level(String issues_access_level) {
        this.issues_access_level = issues_access_level;
    }

    public String getRepository_access_level() {
        return repository_access_level;
    }

    public void setRepository_access_level(String repository_access_level) {
        this.repository_access_level = repository_access_level;
    }

    public String getMerge_requests_access_level() {
        return merge_requests_access_level;
    }

    public void setMerge_requests_access_level(String merge_requests_access_level) {
        this.merge_requests_access_level = merge_requests_access_level;
    }

    public String getForking_access_level() {
        return forking_access_level;
    }

    public void setForking_access_level(String forking_access_level) {
        this.forking_access_level = forking_access_level;
    }

    public String getWiki_access_level() {
        return wiki_access_level;
    }

    public void setWiki_access_level(String wiki_access_level) {
        this.wiki_access_level = wiki_access_level;
    }

    public String getBuilds_access_level() {
        return builds_access_level;
    }

    public void setBuilds_access_level(String builds_access_level) {
        this.builds_access_level = builds_access_level;
    }

    public String getSnippets_access_level() {
        return snippets_access_level;
    }

    public void setSnippets_access_level(String snippets_access_level) {
        this.snippets_access_level = snippets_access_level;
    }

    public String getPages_access_level() {
        return pages_access_level;
    }

    public void setPages_access_level(String pages_access_level) {
        this.pages_access_level = pages_access_level;
    }

    public String getOperations_access_level() {
        return operations_access_level;
    }

    public void setOperations_access_level(String operations_access_level) {
        this.operations_access_level = operations_access_level;
    }

    public String getAnalytics_access_level() {
        return analytics_access_level;
    }

    public void setAnalytics_access_level(String analytics_access_level) {
        this.analytics_access_level = analytics_access_level;
    }

    public Object getEmails_disabled() {
        return emails_disabled;
    }

    public void setEmails_disabled(Object emails_disabled) {
        this.emails_disabled = emails_disabled;
    }

    public boolean isShared_runners_enabled() {
        return shared_runners_enabled;
    }

    public void setShared_runners_enabled(boolean shared_runners_enabled) {
        this.shared_runners_enabled = shared_runners_enabled;
    }

    public boolean isLfs_enabled() {
        return lfs_enabled;
    }

    public void setLfs_enabled(boolean lfs_enabled) {
        this.lfs_enabled = lfs_enabled;
    }

    public int getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(int creator_id) {
        this.creator_id = creator_id;
    }

    public String getImport_status() {
        return import_status;
    }

    public void setImport_status(String import_status) {
        this.import_status = import_status;
    }

    public int getOpen_issues_count() {
        return open_issues_count;
    }

    public void setOpen_issues_count(int open_issues_count) {
        this.open_issues_count = open_issues_count;
    }

    public int getCi_default_git_depth() {
        return ci_default_git_depth;
    }

    public void setCi_default_git_depth(int ci_default_git_depth) {
        this.ci_default_git_depth = ci_default_git_depth;
    }

    public boolean isCi_forward_deployment_enabled() {
        return ci_forward_deployment_enabled;
    }

    public void setCi_forward_deployment_enabled(boolean ci_forward_deployment_enabled) {
        this.ci_forward_deployment_enabled = ci_forward_deployment_enabled;
    }

    public boolean isPublic_jobs() {
        return public_jobs;
    }

    public void setPublic_jobs(boolean public_jobs) {
        this.public_jobs = public_jobs;
    }

    public int getBuild_timeout() {
        return build_timeout;
    }

    public void setBuild_timeout(int build_timeout) {
        this.build_timeout = build_timeout;
    }

    public String getAuto_cancel_pending_pipelines() {
        return auto_cancel_pending_pipelines;
    }

    public void setAuto_cancel_pending_pipelines(String auto_cancel_pending_pipelines) {
        this.auto_cancel_pending_pipelines = auto_cancel_pending_pipelines;
    }

    public Object getBuild_coverage_regex() {
        return build_coverage_regex;
    }

    public void setBuild_coverage_regex(Object build_coverage_regex) {
        this.build_coverage_regex = build_coverage_regex;
    }

    public Object getCi_config_path() {
        return ci_config_path;
    }

    public void setCi_config_path(Object ci_config_path) {
        this.ci_config_path = ci_config_path;
    }

    public boolean isOnly_allow_merge_if_pipeline_succeeds() {
        return only_allow_merge_if_pipeline_succeeds;
    }

    public void setOnly_allow_merge_if_pipeline_succeeds(boolean only_allow_merge_if_pipeline_succeeds) {
        this.only_allow_merge_if_pipeline_succeeds = only_allow_merge_if_pipeline_succeeds;
    }

    public Object getAllow_merge_on_skipped_pipeline() {
        return allow_merge_on_skipped_pipeline;
    }

    public void setAllow_merge_on_skipped_pipeline(Object allow_merge_on_skipped_pipeline) {
        this.allow_merge_on_skipped_pipeline = allow_merge_on_skipped_pipeline;
    }

    public boolean isRestrict_user_defined_variables() {
        return restrict_user_defined_variables;
    }

    public void setRestrict_user_defined_variables(boolean restrict_user_defined_variables) {
        this.restrict_user_defined_variables = restrict_user_defined_variables;
    }

    public boolean isRequest_access_enabled() {
        return request_access_enabled;
    }

    public void setRequest_access_enabled(boolean request_access_enabled) {
        this.request_access_enabled = request_access_enabled;
    }

    public boolean isOnly_allow_merge_if_all_discussions_are_resolved() {
        return only_allow_merge_if_all_discussions_are_resolved;
    }

    public void setOnly_allow_merge_if_all_discussions_are_resolved(boolean only_allow_merge_if_all_discussions_are_resolved) {
        this.only_allow_merge_if_all_discussions_are_resolved = only_allow_merge_if_all_discussions_are_resolved;
    }

    public boolean isRemove_source_branch_after_merge() {
        return remove_source_branch_after_merge;
    }

    public void setRemove_source_branch_after_merge(boolean remove_source_branch_after_merge) {
        this.remove_source_branch_after_merge = remove_source_branch_after_merge;
    }

    public boolean isPrinting_merge_request_link_enabled() {
        return printing_merge_request_link_enabled;
    }

    public void setPrinting_merge_request_link_enabled(boolean printing_merge_request_link_enabled) {
        this.printing_merge_request_link_enabled = printing_merge_request_link_enabled;
    }

    public String getMerge_method() {
        return merge_method;
    }

    public void setMerge_method(String merge_method) {
        this.merge_method = merge_method;
    }

    public Object getSuggestion_commit_message() {
        return suggestion_commit_message;
    }

    public void setSuggestion_commit_message(Object suggestion_commit_message) {
        this.suggestion_commit_message = suggestion_commit_message;
    }

    public boolean isAuto_devops_enabled() {
        return auto_devops_enabled;
    }

    public void setAuto_devops_enabled(boolean auto_devops_enabled) {
        this.auto_devops_enabled = auto_devops_enabled;
    }

    public String getAuto_devops_deploy_strategy() {
        return auto_devops_deploy_strategy;
    }

    public void setAuto_devops_deploy_strategy(String auto_devops_deploy_strategy) {
        this.auto_devops_deploy_strategy = auto_devops_deploy_strategy;
    }

    public boolean isAutoclose_referenced_issues() {
        return autoclose_referenced_issues;
    }

    public void setAutoclose_referenced_issues(boolean autoclose_referenced_issues) {
        this.autoclose_referenced_issues = autoclose_referenced_issues;
    }

    public PermissionsBean getPermissions() {
        return permissions;
    }

    public void setPermissions(PermissionsBean permissions) {
        this.permissions = permissions;
    }

    public List<?> getTag_list() {
        return tag_list;
    }

    public void setTag_list(List<?> tag_list) {
        this.tag_list = tag_list;
    }

    public List<?> getShared_with_groups() {
        return shared_with_groups;
    }

    public void setShared_with_groups(List<?> shared_with_groups) {
        this.shared_with_groups = shared_with_groups;
    }

    public static class NamespaceBean {
        private int id;
        private String name;
        private String path;
        private String kind;
        private String full_path;
        private Object parent_id;
        private Object avatar_url;
        private String web_url;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getKind() {
            return kind;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        public String getFull_path() {
            return full_path;
        }

        public void setFull_path(String full_path) {
            this.full_path = full_path;
        }

        public Object getParent_id() {
            return parent_id;
        }

        public void setParent_id(Object parent_id) {
            this.parent_id = parent_id;
        }

        public Object getAvatar_url() {
            return avatar_url;
        }

        public void setAvatar_url(Object avatar_url) {
            this.avatar_url = avatar_url;
        }

        public String getWeb_url() {
            return web_url;
        }

        public void setWeb_url(String web_url) {
            this.web_url = web_url;
        }
    }

    public static class LinksBean {
        private String self;
        private String issues;
        private String merge_requests;
        private String repo_branches;
        private String labels;
        private String events;
        private String members;

        public String getSelf() {
            return self;
        }

        public void setSelf(String self) {
            this.self = self;
        }

        public String getIssues() {
            return issues;
        }

        public void setIssues(String issues) {
            this.issues = issues;
        }

        public String getMerge_requests() {
            return merge_requests;
        }

        public void setMerge_requests(String merge_requests) {
            this.merge_requests = merge_requests;
        }

        public String getRepo_branches() {
            return repo_branches;
        }

        public void setRepo_branches(String repo_branches) {
            this.repo_branches = repo_branches;
        }

        public String getLabels() {
            return labels;
        }

        public void setLabels(String labels) {
            this.labels = labels;
        }

        public String getEvents() {
            return events;
        }

        public void setEvents(String events) {
            this.events = events;
        }

        public String getMembers() {
            return members;
        }

        public void setMembers(String members) {
            this.members = members;
        }
    }

    public static class ContainerExpirationPolicyBean {
        private String cadence;
        private boolean enabled;
        private int keep_n;
        private String older_than;
        private String name_regex;
        private Object name_regex_keep;
        private String next_run_at;

        public String getCadence() {
            return cadence;
        }

        public void setCadence(String cadence) {
            this.cadence = cadence;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public int getKeep_n() {
            return keep_n;
        }

        public void setKeep_n(int keep_n) {
            this.keep_n = keep_n;
        }

        public String getOlder_than() {
            return older_than;
        }

        public void setOlder_than(String older_than) {
            this.older_than = older_than;
        }

        public String getName_regex() {
            return name_regex;
        }

        public void setName_regex(String name_regex) {
            this.name_regex = name_regex;
        }

        public Object getName_regex_keep() {
            return name_regex_keep;
        }

        public void setName_regex_keep(Object name_regex_keep) {
            this.name_regex_keep = name_regex_keep;
        }

        public String getNext_run_at() {
            return next_run_at;
        }

        public void setNext_run_at(String next_run_at) {
            this.next_run_at = next_run_at;
        }
    }

    public static class PermissionsBean {
        private ProjectAccessBean project_access;
        private GroupAccessBean group_access;

        public ProjectAccessBean getProject_access() {
            return project_access;
        }

        public void setProject_access(ProjectAccessBean project_access) {
            this.project_access = project_access;
        }

        public GroupAccessBean getGroup_access() {
            return group_access;
        }

        public void setGroup_access(GroupAccessBean group_access) {
            this.group_access = group_access;
        }

        public static class ProjectAccessBean {
            private int access_level;
            private int notification_level;

            public int getAccess_level() {
                return access_level;
            }

            public void setAccess_level(int access_level) {
                this.access_level = access_level;
            }

            public int getNotification_level() {
                return notification_level;
            }

            public void setNotification_level(int notification_level) {
                this.notification_level = notification_level;
            }
        }

        public static class GroupAccessBean {
            private int access_level;
            private int notification_level;

            public int getAccess_level() {
                return access_level;
            }

            public void setAccess_level(int access_level) {
                this.access_level = access_level;
            }

            public int getNotification_level() {
                return notification_level;
            }

            public void setNotification_level(int notification_level) {
                this.notification_level = notification_level;
            }
        }
    }
}
