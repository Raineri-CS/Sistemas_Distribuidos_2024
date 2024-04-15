package org.gradle.accessors.dm;

import org.gradle.api.NonNullApi;
import org.gradle.api.artifacts.MinimalExternalModuleDependency;
import org.gradle.plugin.use.PluginDependency;
import org.gradle.api.artifacts.ExternalModuleDependencyBundle;
import org.gradle.api.artifacts.MutableVersionConstraint;
import org.gradle.api.provider.Provider;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.api.internal.catalog.AbstractExternalDependencyFactory;
import org.gradle.api.internal.catalog.DefaultVersionCatalog;
import java.util.Map;
import org.gradle.api.internal.attributes.ImmutableAttributesFactory;
import org.gradle.api.internal.artifacts.dsl.CapabilityNotationParser;
import javax.inject.Inject;

/**
 * A catalog of dependencies accessible via the `libs` extension.
 */
@NonNullApi
public class LibrariesForLibs extends AbstractExternalDependencyFactory {

    private final AbstractExternalDependencyFactory owner = this;
    private final ComLibraryAccessors laccForComLibraryAccessors = new ComLibraryAccessors(owner);
    private final EuLibraryAccessors laccForEuLibraryAccessors = new EuLibraryAccessors(owner);
    private final NetLibraryAccessors laccForNetLibraryAccessors = new NetLibraryAccessors(owner);
    private final OrgLibraryAccessors laccForOrgLibraryAccessors = new OrgLibraryAccessors(owner);
    private final VersionAccessors vaccForVersionAccessors = new VersionAccessors(providers, config);
    private final BundleAccessors baccForBundleAccessors = new BundleAccessors(objects, providers, config, attributesFactory, capabilityNotationParser);
    private final PluginAccessors paccForPluginAccessors = new PluginAccessors(providers, config);

    @Inject
    public LibrariesForLibs(DefaultVersionCatalog config, ProviderFactory providers, ObjectFactory objects, ImmutableAttributesFactory attributesFactory, CapabilityNotationParser capabilityNotationParser) {
        super(config, providers, objects, attributesFactory, capabilityNotationParser);
    }

    /**
     * Returns the group of libraries at com
     */
    public ComLibraryAccessors getCom() {
        return laccForComLibraryAccessors;
    }

    /**
     * Returns the group of libraries at eu
     */
    public EuLibraryAccessors getEu() {
        return laccForEuLibraryAccessors;
    }

    /**
     * Returns the group of libraries at net
     */
    public NetLibraryAccessors getNet() {
        return laccForNetLibraryAccessors;
    }

    /**
     * Returns the group of libraries at org
     */
    public OrgLibraryAccessors getOrg() {
        return laccForOrgLibraryAccessors;
    }

    /**
     * Returns the group of versions at versions
     */
    public VersionAccessors getVersions() {
        return vaccForVersionAccessors;
    }

    /**
     * Returns the group of bundles at bundles
     */
    public BundleAccessors getBundles() {
        return baccForBundleAccessors;
    }

    /**
     * Returns the group of plugins at plugins
     */
    public PluginAccessors getPlugins() {
        return paccForPluginAccessors;
    }

    public static class ComLibraryAccessors extends SubDependencyFactory {
        private final ComDlscLibraryAccessors laccForComDlscLibraryAccessors = new ComDlscLibraryAccessors(owner);
        private final ComGithubLibraryAccessors laccForComGithubLibraryAccessors = new ComGithubLibraryAccessors(owner);

        public ComLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at com.dlsc
         */
        public ComDlscLibraryAccessors getDlsc() {
            return laccForComDlscLibraryAccessors;
        }

        /**
         * Returns the group of libraries at com.github
         */
        public ComGithubLibraryAccessors getGithub() {
            return laccForComGithubLibraryAccessors;
        }

    }

    public static class ComDlscLibraryAccessors extends SubDependencyFactory {
        private final ComDlscFormsfxLibraryAccessors laccForComDlscFormsfxLibraryAccessors = new ComDlscFormsfxLibraryAccessors(owner);

        public ComDlscLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at com.dlsc.formsfx
         */
        public ComDlscFormsfxLibraryAccessors getFormsfx() {
            return laccForComDlscFormsfxLibraryAccessors;
        }

    }

    public static class ComDlscFormsfxLibraryAccessors extends SubDependencyFactory {
        private final ComDlscFormsfxFormsfxLibraryAccessors laccForComDlscFormsfxFormsfxLibraryAccessors = new ComDlscFormsfxFormsfxLibraryAccessors(owner);

        public ComDlscFormsfxLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at com.dlsc.formsfx.formsfx
         */
        public ComDlscFormsfxFormsfxLibraryAccessors getFormsfx() {
            return laccForComDlscFormsfxFormsfxLibraryAccessors;
        }

    }

    public static class ComDlscFormsfxFormsfxLibraryAccessors extends SubDependencyFactory {

        public ComDlscFormsfxFormsfxLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for core (com.dlsc.formsfx:formsfx-core)
         * with versionRef 'com.dlsc.formsfx.formsfx.core'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getCore() {
                return create("com.dlsc.formsfx.formsfx.core");
        }

    }

    public static class ComGithubLibraryAccessors extends SubDependencyFactory {
        private final ComGithubAlmasbLibraryAccessors laccForComGithubAlmasbLibraryAccessors = new ComGithubAlmasbLibraryAccessors(owner);

        public ComGithubLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at com.github.almasb
         */
        public ComGithubAlmasbLibraryAccessors getAlmasb() {
            return laccForComGithubAlmasbLibraryAccessors;
        }

    }

    public static class ComGithubAlmasbLibraryAccessors extends SubDependencyFactory {

        public ComGithubAlmasbLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for fxgl (com.github.almasb:fxgl)
         * with versionRef 'com.github.almasb.fxgl'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getFxgl() {
                return create("com.github.almasb.fxgl");
        }

    }

    public static class EuLibraryAccessors extends SubDependencyFactory {
        private final EuHansoloLibraryAccessors laccForEuHansoloLibraryAccessors = new EuHansoloLibraryAccessors(owner);

        public EuLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at eu.hansolo
         */
        public EuHansoloLibraryAccessors getHansolo() {
            return laccForEuHansoloLibraryAccessors;
        }

    }

    public static class EuHansoloLibraryAccessors extends SubDependencyFactory {

        public EuHansoloLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for tilesfx (eu.hansolo:tilesfx)
         * with versionRef 'eu.hansolo.tilesfx'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getTilesfx() {
                return create("eu.hansolo.tilesfx");
        }

    }

    public static class NetLibraryAccessors extends SubDependencyFactory {
        private final NetSynedraLibraryAccessors laccForNetSynedraLibraryAccessors = new NetSynedraLibraryAccessors(owner);

        public NetLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at net.synedra
         */
        public NetSynedraLibraryAccessors getSynedra() {
            return laccForNetSynedraLibraryAccessors;
        }

    }

    public static class NetSynedraLibraryAccessors extends SubDependencyFactory {

        public NetSynedraLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for validatorfx (net.synedra:validatorfx)
         * with versionRef 'net.synedra.validatorfx'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getValidatorfx() {
                return create("net.synedra.validatorfx");
        }

    }

    public static class OrgLibraryAccessors extends SubDependencyFactory {
        private final OrgControlsfxLibraryAccessors laccForOrgControlsfxLibraryAccessors = new OrgControlsfxLibraryAccessors(owner);
        private final OrgJunitLibraryAccessors laccForOrgJunitLibraryAccessors = new OrgJunitLibraryAccessors(owner);
        private final OrgKordampLibraryAccessors laccForOrgKordampLibraryAccessors = new OrgKordampLibraryAccessors(owner);
        private final OrgOpenjfxLibraryAccessors laccForOrgOpenjfxLibraryAccessors = new OrgOpenjfxLibraryAccessors(owner);

        public OrgLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at org.controlsfx
         */
        public OrgControlsfxLibraryAccessors getControlsfx() {
            return laccForOrgControlsfxLibraryAccessors;
        }

        /**
         * Returns the group of libraries at org.junit
         */
        public OrgJunitLibraryAccessors getJunit() {
            return laccForOrgJunitLibraryAccessors;
        }

        /**
         * Returns the group of libraries at org.kordamp
         */
        public OrgKordampLibraryAccessors getKordamp() {
            return laccForOrgKordampLibraryAccessors;
        }

        /**
         * Returns the group of libraries at org.openjfx
         */
        public OrgOpenjfxLibraryAccessors getOpenjfx() {
            return laccForOrgOpenjfxLibraryAccessors;
        }

    }

    public static class OrgControlsfxLibraryAccessors extends SubDependencyFactory {

        public OrgControlsfxLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for controlsfx (org.controlsfx:controlsfx)
         * with versionRef 'org.controlsfx.controlsfx'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getControlsfx() {
                return create("org.controlsfx.controlsfx");
        }

    }

    public static class OrgJunitLibraryAccessors extends SubDependencyFactory {
        private final OrgJunitJupiterLibraryAccessors laccForOrgJunitJupiterLibraryAccessors = new OrgJunitJupiterLibraryAccessors(owner);

        public OrgJunitLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at org.junit.jupiter
         */
        public OrgJunitJupiterLibraryAccessors getJupiter() {
            return laccForOrgJunitJupiterLibraryAccessors;
        }

    }

    public static class OrgJunitJupiterLibraryAccessors extends SubDependencyFactory {
        private final OrgJunitJupiterJunitLibraryAccessors laccForOrgJunitJupiterJunitLibraryAccessors = new OrgJunitJupiterJunitLibraryAccessors(owner);

        public OrgJunitJupiterLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at org.junit.jupiter.junit
         */
        public OrgJunitJupiterJunitLibraryAccessors getJunit() {
            return laccForOrgJunitJupiterJunitLibraryAccessors;
        }

    }

    public static class OrgJunitJupiterJunitLibraryAccessors extends SubDependencyFactory {
        private final OrgJunitJupiterJunitJupiterLibraryAccessors laccForOrgJunitJupiterJunitJupiterLibraryAccessors = new OrgJunitJupiterJunitJupiterLibraryAccessors(owner);

        public OrgJunitJupiterJunitLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at org.junit.jupiter.junit.jupiter
         */
        public OrgJunitJupiterJunitJupiterLibraryAccessors getJupiter() {
            return laccForOrgJunitJupiterJunitJupiterLibraryAccessors;
        }

    }

    public static class OrgJunitJupiterJunitJupiterLibraryAccessors extends SubDependencyFactory {

        public OrgJunitJupiterJunitJupiterLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for api (org.junit.jupiter:junit-jupiter-api)
         * with versionRef 'org.junit.jupiter.junit.jupiter.api'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getApi() {
                return create("org.junit.jupiter.junit.jupiter.api");
        }

            /**
             * Creates a dependency provider for engine (org.junit.jupiter:junit-jupiter-engine)
         * with versionRef 'org.junit.jupiter.junit.jupiter.engine'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getEngine() {
                return create("org.junit.jupiter.junit.jupiter.engine");
        }

    }

    public static class OrgKordampLibraryAccessors extends SubDependencyFactory {
        private final OrgKordampBootstrapfxLibraryAccessors laccForOrgKordampBootstrapfxLibraryAccessors = new OrgKordampBootstrapfxLibraryAccessors(owner);
        private final OrgKordampIkonliLibraryAccessors laccForOrgKordampIkonliLibraryAccessors = new OrgKordampIkonliLibraryAccessors(owner);

        public OrgKordampLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at org.kordamp.bootstrapfx
         */
        public OrgKordampBootstrapfxLibraryAccessors getBootstrapfx() {
            return laccForOrgKordampBootstrapfxLibraryAccessors;
        }

        /**
         * Returns the group of libraries at org.kordamp.ikonli
         */
        public OrgKordampIkonliLibraryAccessors getIkonli() {
            return laccForOrgKordampIkonliLibraryAccessors;
        }

    }

    public static class OrgKordampBootstrapfxLibraryAccessors extends SubDependencyFactory {
        private final OrgKordampBootstrapfxBootstrapfxLibraryAccessors laccForOrgKordampBootstrapfxBootstrapfxLibraryAccessors = new OrgKordampBootstrapfxBootstrapfxLibraryAccessors(owner);

        public OrgKordampBootstrapfxLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at org.kordamp.bootstrapfx.bootstrapfx
         */
        public OrgKordampBootstrapfxBootstrapfxLibraryAccessors getBootstrapfx() {
            return laccForOrgKordampBootstrapfxBootstrapfxLibraryAccessors;
        }

    }

    public static class OrgKordampBootstrapfxBootstrapfxLibraryAccessors extends SubDependencyFactory {

        public OrgKordampBootstrapfxBootstrapfxLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for core (org.kordamp.bootstrapfx:bootstrapfx-core)
         * with versionRef 'org.kordamp.bootstrapfx.bootstrapfx.core'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getCore() {
                return create("org.kordamp.bootstrapfx.bootstrapfx.core");
        }

    }

    public static class OrgKordampIkonliLibraryAccessors extends SubDependencyFactory {
        private final OrgKordampIkonliIkonliLibraryAccessors laccForOrgKordampIkonliIkonliLibraryAccessors = new OrgKordampIkonliIkonliLibraryAccessors(owner);

        public OrgKordampIkonliLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at org.kordamp.ikonli.ikonli
         */
        public OrgKordampIkonliIkonliLibraryAccessors getIkonli() {
            return laccForOrgKordampIkonliIkonliLibraryAccessors;
        }

    }

    public static class OrgKordampIkonliIkonliLibraryAccessors extends SubDependencyFactory {

        public OrgKordampIkonliIkonliLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for javafx (org.kordamp.ikonli:ikonli-javafx)
         * with versionRef 'org.kordamp.ikonli.ikonli.javafx'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getJavafx() {
                return create("org.kordamp.ikonli.ikonli.javafx");
        }

    }

    public static class OrgOpenjfxLibraryAccessors extends SubDependencyFactory {
        private final OrgOpenjfxJavafxLibraryAccessors laccForOrgOpenjfxJavafxLibraryAccessors = new OrgOpenjfxJavafxLibraryAccessors(owner);

        public OrgOpenjfxLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at org.openjfx.javafx
         */
        public OrgOpenjfxJavafxLibraryAccessors getJavafx() {
            return laccForOrgOpenjfxJavafxLibraryAccessors;
        }

    }

    public static class OrgOpenjfxJavafxLibraryAccessors extends SubDependencyFactory {

        public OrgOpenjfxJavafxLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for controls (org.openjfx:javafx-controls)
         * with versionRef 'org.openjfx.javafx.controls'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getControls() {
                return create("org.openjfx.javafx.controls");
        }

            /**
             * Creates a dependency provider for fxml (org.openjfx:javafx-fxml)
         * with versionRef 'org.openjfx.javafx.fxml'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getFxml() {
                return create("org.openjfx.javafx.fxml");
        }

            /**
             * Creates a dependency provider for media (org.openjfx:javafx-media)
         * with versionRef 'org.openjfx.javafx.media'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getMedia() {
                return create("org.openjfx.javafx.media");
        }

            /**
             * Creates a dependency provider for swing (org.openjfx:javafx-swing)
         * with versionRef 'org.openjfx.javafx.swing'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getSwing() {
                return create("org.openjfx.javafx.swing");
        }

            /**
             * Creates a dependency provider for web (org.openjfx:javafx-web)
         * with versionRef 'org.openjfx.javafx.web'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getWeb() {
                return create("org.openjfx.javafx.web");
        }

    }

    public static class VersionAccessors extends VersionFactory  {

        private final ComVersionAccessors vaccForComVersionAccessors = new ComVersionAccessors(providers, config);
        private final EuVersionAccessors vaccForEuVersionAccessors = new EuVersionAccessors(providers, config);
        private final NetVersionAccessors vaccForNetVersionAccessors = new NetVersionAccessors(providers, config);
        private final OrgVersionAccessors vaccForOrgVersionAccessors = new OrgVersionAccessors(providers, config);
        public VersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.com
         */
        public ComVersionAccessors getCom() {
            return vaccForComVersionAccessors;
        }

        /**
         * Returns the group of versions at versions.eu
         */
        public EuVersionAccessors getEu() {
            return vaccForEuVersionAccessors;
        }

        /**
         * Returns the group of versions at versions.net
         */
        public NetVersionAccessors getNet() {
            return vaccForNetVersionAccessors;
        }

        /**
         * Returns the group of versions at versions.org
         */
        public OrgVersionAccessors getOrg() {
            return vaccForOrgVersionAccessors;
        }

    }

    public static class ComVersionAccessors extends VersionFactory  {

        private final ComDlscVersionAccessors vaccForComDlscVersionAccessors = new ComDlscVersionAccessors(providers, config);
        private final ComGithubVersionAccessors vaccForComGithubVersionAccessors = new ComGithubVersionAccessors(providers, config);
        public ComVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.com.dlsc
         */
        public ComDlscVersionAccessors getDlsc() {
            return vaccForComDlscVersionAccessors;
        }

        /**
         * Returns the group of versions at versions.com.github
         */
        public ComGithubVersionAccessors getGithub() {
            return vaccForComGithubVersionAccessors;
        }

    }

    public static class ComDlscVersionAccessors extends VersionFactory  {

        private final ComDlscFormsfxVersionAccessors vaccForComDlscFormsfxVersionAccessors = new ComDlscFormsfxVersionAccessors(providers, config);
        public ComDlscVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.com.dlsc.formsfx
         */
        public ComDlscFormsfxVersionAccessors getFormsfx() {
            return vaccForComDlscFormsfxVersionAccessors;
        }

    }

    public static class ComDlscFormsfxVersionAccessors extends VersionFactory  {

        private final ComDlscFormsfxFormsfxVersionAccessors vaccForComDlscFormsfxFormsfxVersionAccessors = new ComDlscFormsfxFormsfxVersionAccessors(providers, config);
        public ComDlscFormsfxVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.com.dlsc.formsfx.formsfx
         */
        public ComDlscFormsfxFormsfxVersionAccessors getFormsfx() {
            return vaccForComDlscFormsfxFormsfxVersionAccessors;
        }

    }

    public static class ComDlscFormsfxFormsfxVersionAccessors extends VersionFactory  {

        public ComDlscFormsfxFormsfxVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: com.dlsc.formsfx.formsfx.core (11.6.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getCore() { return getVersion("com.dlsc.formsfx.formsfx.core"); }

    }

    public static class ComGithubVersionAccessors extends VersionFactory  {

        private final ComGithubAlmasbVersionAccessors vaccForComGithubAlmasbVersionAccessors = new ComGithubAlmasbVersionAccessors(providers, config);
        public ComGithubVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.com.github.almasb
         */
        public ComGithubAlmasbVersionAccessors getAlmasb() {
            return vaccForComGithubAlmasbVersionAccessors;
        }

    }

    public static class ComGithubAlmasbVersionAccessors extends VersionFactory  {

        public ComGithubAlmasbVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: com.github.almasb.fxgl (17.3)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getFxgl() { return getVersion("com.github.almasb.fxgl"); }

    }

    public static class EuVersionAccessors extends VersionFactory  {

        private final EuHansoloVersionAccessors vaccForEuHansoloVersionAccessors = new EuHansoloVersionAccessors(providers, config);
        public EuVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.eu.hansolo
         */
        public EuHansoloVersionAccessors getHansolo() {
            return vaccForEuHansoloVersionAccessors;
        }

    }

    public static class EuHansoloVersionAccessors extends VersionFactory  {

        public EuHansoloVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: eu.hansolo.tilesfx (11.48)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getTilesfx() { return getVersion("eu.hansolo.tilesfx"); }

    }

    public static class NetVersionAccessors extends VersionFactory  {

        private final NetSynedraVersionAccessors vaccForNetSynedraVersionAccessors = new NetSynedraVersionAccessors(providers, config);
        public NetVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.net.synedra
         */
        public NetSynedraVersionAccessors getSynedra() {
            return vaccForNetSynedraVersionAccessors;
        }

    }

    public static class NetSynedraVersionAccessors extends VersionFactory  {

        public NetSynedraVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: net.synedra.validatorfx (0.4.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getValidatorfx() { return getVersion("net.synedra.validatorfx"); }

    }

    public static class OrgVersionAccessors extends VersionFactory  {

        private final OrgControlsfxVersionAccessors vaccForOrgControlsfxVersionAccessors = new OrgControlsfxVersionAccessors(providers, config);
        private final OrgJunitVersionAccessors vaccForOrgJunitVersionAccessors = new OrgJunitVersionAccessors(providers, config);
        private final OrgKordampVersionAccessors vaccForOrgKordampVersionAccessors = new OrgKordampVersionAccessors(providers, config);
        private final OrgOpenjfxVersionAccessors vaccForOrgOpenjfxVersionAccessors = new OrgOpenjfxVersionAccessors(providers, config);
        public OrgVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.org.controlsfx
         */
        public OrgControlsfxVersionAccessors getControlsfx() {
            return vaccForOrgControlsfxVersionAccessors;
        }

        /**
         * Returns the group of versions at versions.org.junit
         */
        public OrgJunitVersionAccessors getJunit() {
            return vaccForOrgJunitVersionAccessors;
        }

        /**
         * Returns the group of versions at versions.org.kordamp
         */
        public OrgKordampVersionAccessors getKordamp() {
            return vaccForOrgKordampVersionAccessors;
        }

        /**
         * Returns the group of versions at versions.org.openjfx
         */
        public OrgOpenjfxVersionAccessors getOpenjfx() {
            return vaccForOrgOpenjfxVersionAccessors;
        }

    }

    public static class OrgControlsfxVersionAccessors extends VersionFactory  {

        public OrgControlsfxVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: org.controlsfx.controlsfx (11.1.2)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getControlsfx() { return getVersion("org.controlsfx.controlsfx"); }

    }

    public static class OrgJunitVersionAccessors extends VersionFactory  {

        private final OrgJunitJupiterVersionAccessors vaccForOrgJunitJupiterVersionAccessors = new OrgJunitJupiterVersionAccessors(providers, config);
        public OrgJunitVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.org.junit.jupiter
         */
        public OrgJunitJupiterVersionAccessors getJupiter() {
            return vaccForOrgJunitJupiterVersionAccessors;
        }

    }

    public static class OrgJunitJupiterVersionAccessors extends VersionFactory  {

        private final OrgJunitJupiterJunitVersionAccessors vaccForOrgJunitJupiterJunitVersionAccessors = new OrgJunitJupiterJunitVersionAccessors(providers, config);
        public OrgJunitJupiterVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.org.junit.jupiter.junit
         */
        public OrgJunitJupiterJunitVersionAccessors getJunit() {
            return vaccForOrgJunitJupiterJunitVersionAccessors;
        }

    }

    public static class OrgJunitJupiterJunitVersionAccessors extends VersionFactory  {

        private final OrgJunitJupiterJunitJupiterVersionAccessors vaccForOrgJunitJupiterJunitJupiterVersionAccessors = new OrgJunitJupiterJunitJupiterVersionAccessors(providers, config);
        public OrgJunitJupiterJunitVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.org.junit.jupiter.junit.jupiter
         */
        public OrgJunitJupiterJunitJupiterVersionAccessors getJupiter() {
            return vaccForOrgJunitJupiterJunitJupiterVersionAccessors;
        }

    }

    public static class OrgJunitJupiterJunitJupiterVersionAccessors extends VersionFactory  {

        public OrgJunitJupiterJunitJupiterVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: org.junit.jupiter.junit.jupiter.api (5.10.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getApi() { return getVersion("org.junit.jupiter.junit.jupiter.api"); }

            /**
             * Returns the version associated to this alias: org.junit.jupiter.junit.jupiter.engine (5.10.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getEngine() { return getVersion("org.junit.jupiter.junit.jupiter.engine"); }

    }

    public static class OrgKordampVersionAccessors extends VersionFactory  {

        private final OrgKordampBootstrapfxVersionAccessors vaccForOrgKordampBootstrapfxVersionAccessors = new OrgKordampBootstrapfxVersionAccessors(providers, config);
        private final OrgKordampIkonliVersionAccessors vaccForOrgKordampIkonliVersionAccessors = new OrgKordampIkonliVersionAccessors(providers, config);
        public OrgKordampVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.org.kordamp.bootstrapfx
         */
        public OrgKordampBootstrapfxVersionAccessors getBootstrapfx() {
            return vaccForOrgKordampBootstrapfxVersionAccessors;
        }

        /**
         * Returns the group of versions at versions.org.kordamp.ikonli
         */
        public OrgKordampIkonliVersionAccessors getIkonli() {
            return vaccForOrgKordampIkonliVersionAccessors;
        }

    }

    public static class OrgKordampBootstrapfxVersionAccessors extends VersionFactory  {

        private final OrgKordampBootstrapfxBootstrapfxVersionAccessors vaccForOrgKordampBootstrapfxBootstrapfxVersionAccessors = new OrgKordampBootstrapfxBootstrapfxVersionAccessors(providers, config);
        public OrgKordampBootstrapfxVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.org.kordamp.bootstrapfx.bootstrapfx
         */
        public OrgKordampBootstrapfxBootstrapfxVersionAccessors getBootstrapfx() {
            return vaccForOrgKordampBootstrapfxBootstrapfxVersionAccessors;
        }

    }

    public static class OrgKordampBootstrapfxBootstrapfxVersionAccessors extends VersionFactory  {

        public OrgKordampBootstrapfxBootstrapfxVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: org.kordamp.bootstrapfx.bootstrapfx.core (0.4.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getCore() { return getVersion("org.kordamp.bootstrapfx.bootstrapfx.core"); }

    }

    public static class OrgKordampIkonliVersionAccessors extends VersionFactory  {

        private final OrgKordampIkonliIkonliVersionAccessors vaccForOrgKordampIkonliIkonliVersionAccessors = new OrgKordampIkonliIkonliVersionAccessors(providers, config);
        public OrgKordampIkonliVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.org.kordamp.ikonli.ikonli
         */
        public OrgKordampIkonliIkonliVersionAccessors getIkonli() {
            return vaccForOrgKordampIkonliIkonliVersionAccessors;
        }

    }

    public static class OrgKordampIkonliIkonliVersionAccessors extends VersionFactory  {

        public OrgKordampIkonliIkonliVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: org.kordamp.ikonli.ikonli.javafx (12.3.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getJavafx() { return getVersion("org.kordamp.ikonli.ikonli.javafx"); }

    }

    public static class OrgOpenjfxVersionAccessors extends VersionFactory  {

        private final OrgOpenjfxJavafxVersionAccessors vaccForOrgOpenjfxJavafxVersionAccessors = new OrgOpenjfxJavafxVersionAccessors(providers, config);
        public OrgOpenjfxVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.org.openjfx.javafx
         */
        public OrgOpenjfxJavafxVersionAccessors getJavafx() {
            return vaccForOrgOpenjfxJavafxVersionAccessors;
        }

    }

    public static class OrgOpenjfxJavafxVersionAccessors extends VersionFactory  {

        public OrgOpenjfxJavafxVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: org.openjfx.javafx.controls (21)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getControls() { return getVersion("org.openjfx.javafx.controls"); }

            /**
             * Returns the version associated to this alias: org.openjfx.javafx.fxml (21)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getFxml() { return getVersion("org.openjfx.javafx.fxml"); }

            /**
             * Returns the version associated to this alias: org.openjfx.javafx.media (21)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getMedia() { return getVersion("org.openjfx.javafx.media"); }

            /**
             * Returns the version associated to this alias: org.openjfx.javafx.swing (21)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getSwing() { return getVersion("org.openjfx.javafx.swing"); }

            /**
             * Returns the version associated to this alias: org.openjfx.javafx.web (21)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getWeb() { return getVersion("org.openjfx.javafx.web"); }

    }

    public static class BundleAccessors extends BundleFactory {

        public BundleAccessors(ObjectFactory objects, ProviderFactory providers, DefaultVersionCatalog config, ImmutableAttributesFactory attributesFactory, CapabilityNotationParser capabilityNotationParser) { super(objects, providers, config, attributesFactory, capabilityNotationParser); }

    }

    public static class PluginAccessors extends PluginFactory {

        public PluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

    }

}
